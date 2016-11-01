package com.elasticjob.demo.register;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.reg.base.CoordinatorRegistryCenter;
import com.elasticjob.demo.AbstractSimpleTimeTaskJob;
import com.elasticjob.demo.JobSchedulerForSpring;

public class SimpleTimeTaskJobRegister implements ApplicationContextAware {

	private static final Logger       log = LoggerFactory.getLogger(SimpleTimeTaskJobRegister.class);

	/** 注册中心 */
	private CoordinatorRegistryCenter regCenter;

	/** spring上下文 */
	private ApplicationContext        applicationContext;

	/** 设置 注册中心
	 * 
	 * @param regCenter
	 *        注册中心 */
	public void setRegCenter(CoordinatorRegistryCenter regCenter) {
		this.regCenter = regCenter;
	}

	/** 初始化注册所有的简单定时任务 */
	public void init() {
		Map<String, AbstractSimpleTimeTaskJob> map = applicationContext.getBeansOfType(AbstractSimpleTimeTaskJob.class);
		for (String key : map.keySet()) {
			AbstractSimpleTimeTaskJob taskJob = map.get(key);
			// 定时任务名称
			String jobName = taskJob.getJobName() == null ? key : taskJob.getJobName();
			// 执行时间表达式
			String cron = taskJob.getCron();
			// 总分片数
			int shardingTotalCount = taskJob.getShardingTotalCount();

			if (StringUtils.isBlank(jobName) || StringUtils.isBlank(cron) || shardingTotalCount == 0) {
				log.error("定时任务配【" + key + "】置错误！请检查相关配置！");
			}
			else {
				// 定义作业核心配置
				JobCoreConfiguration simpleCoreConfig = JobCoreConfiguration.newBuilder(jobName, cron, shardingTotalCount).build();
				// 定义SIMPLE类型配置
				SimpleJobConfiguration simpleJobConfig = new SimpleJobConfiguration(simpleCoreConfig, taskJob.getClass().getCanonicalName());
				// 定义Lite作业根配置
				LiteJobConfiguration jobRootConfig = LiteJobConfiguration.newBuilder(simpleJobConfig).build();
				// 启动作业
				new JobSchedulerForSpring(regCenter, jobRootConfig, new BaseElasticJobListener()).init(applicationContext);
			}
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}


}
