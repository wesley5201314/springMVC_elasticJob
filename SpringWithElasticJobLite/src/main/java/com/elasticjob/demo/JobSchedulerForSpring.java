package com.elasticjob.demo;

import java.util.Arrays;
import java.util.Properties;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.ApplicationContext;

import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.api.script.ScriptJob;
import com.dangdang.ddframe.job.exception.JobConfigurationException;
import com.dangdang.ddframe.job.exception.JobSystemException;
import com.dangdang.ddframe.job.executor.JobExecutorFactory;
import com.dangdang.ddframe.job.executor.JobFacade;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.internal.executor.JobExecutor;
import com.dangdang.ddframe.job.lite.internal.schedule.JobRegistry;
import com.dangdang.ddframe.job.lite.internal.schedule.JobScheduleController;
import com.dangdang.ddframe.job.lite.internal.schedule.LiteJobFacade;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;

import lombok.Setter;

/**
 * 扩展可以支持spring自动注入
 * @author wesley
 *
 */
public class JobSchedulerForSpring { 

	public static final String  ELASTIC_JOB_DATA_MAP_KEY = "elasticJob";

	private static final String JOB_FACADE_DATA_MAP_KEY  = "jobFacade";

	private final JobExecutor   jobExecutor;

	private final JobFacade     jobFacade;

	public JobSchedulerForSpring(final CoordinatorRegistryCenter regCenter, final LiteJobConfiguration liteJobConfig, final ElasticJobListener... elasticJobListeners) {
		jobExecutor = new JobExecutor(regCenter, liteJobConfig, elasticJobListeners);
		jobFacade = new LiteJobFacade(regCenter, liteJobConfig.getJobName(), Arrays.asList(elasticJobListeners));
	}

	/** 初始化作业. */
	public void init(ApplicationContext applicationContext) {
		jobExecutor.init();
		JobDetail jobDetail = JobBuilder.newJob(LiteJob.class).withIdentity(jobExecutor.getLiteJobConfig().getJobName()).build();
		try {
			if (!jobExecutor.getLiteJobConfig().getTypeConfig().getJobClass().equals(ScriptJob.class.getCanonicalName())) {
				//源码默认的加载类方式
				//jobDetail.getJobDataMap().put(ELASTIC_JOB_DATA_MAP_KEY, Class.forName(jobExecutor.getLiteJobConfig().getTypeConfig().getJobClass()).newInstance());
				//修改为从spring的上下文获取对应的实例
				jobDetail.getJobDataMap().put(ELASTIC_JOB_DATA_MAP_KEY, applicationContext.getBean(Class.forName(jobExecutor.getLiteJobConfig().getTypeConfig().getJobClass())));
				System.out.println("----JobSchedulerForSpring init----"+jobDetail.getJobDataMap().get(ELASTIC_JOB_DATA_MAP_KEY));
			}
		}
		catch (final ReflectiveOperationException ex) {
			throw new JobConfigurationException("Elastic-Job: Job class '%s' can not initialize.", jobExecutor.getLiteJobConfig().getTypeConfig().getJobClass());
		}
		jobDetail.getJobDataMap().put(JOB_FACADE_DATA_MAP_KEY, jobFacade);
		JobScheduleController jobScheduleController;
		try {
			jobScheduleController = new JobScheduleController(initializeScheduler(jobDetail.getKey().toString()), jobDetail, jobExecutor.getSchedulerFacade(), jobExecutor.getLiteJobConfig()
			        .getJobName());
			jobScheduleController.scheduleJob(jobExecutor.getSchedulerFacade().loadJobConfiguration().getTypeConfig().getCoreConfig().getCron());
		}
		catch (final SchedulerException ex) {
			throw new JobSystemException(ex);
		}
		JobRegistry.getInstance().addJobScheduleController(jobExecutor.getLiteJobConfig().getJobName(), jobScheduleController);
	}

	private Scheduler initializeScheduler(final String jobName) throws SchedulerException {
		StdSchedulerFactory factory = new StdSchedulerFactory();
		factory.initialize(getBaseQuartzProperties(jobName));
		Scheduler result = factory.getScheduler();
		result.getListenerManager().addTriggerListener(jobExecutor.getSchedulerFacade().newJobTriggerListener());
		return result;
	}

	private Properties getBaseQuartzProperties(final String jobName) {
		Properties result = new Properties();
		result.put("org.quartz.threadPool.class", org.quartz.simpl.SimpleThreadPool.class.getName());
		result.put("org.quartz.threadPool.threadCount", "1");
		result.put("org.quartz.scheduler.instanceName", jobName);
		if (!jobExecutor.getSchedulerFacade().loadJobConfiguration().getTypeConfig().getCoreConfig().isMisfire()) {
			result.put("org.quartz.jobStore.misfireThreshold", "1");
		}
		prepareEnvironments(result);
		return result;
	}

	protected void prepareEnvironments(final Properties props) {
	}

	/** 停止作业调度. */
	public void shutdown() {
		JobRegistry.getInstance().getJobScheduleController(jobExecutor.getLiteJobConfig().getJobName()).shutdown();
	}

	/** Lite调度作业.
	 * 
	 * @author zhangliang */
	public static final class LiteJob implements Job {

		@Setter
		private ElasticJob elasticJob;

		@Setter
		private JobFacade  jobFacade;

		@Override
		public void execute(final JobExecutionContext context) throws JobExecutionException {
			System.out.println("---------------LiteJob---------start---"+elasticJob);
			JobExecutorFactory.getJobExecutor(elasticJob, jobFacade).execute();
		}
	}
}
