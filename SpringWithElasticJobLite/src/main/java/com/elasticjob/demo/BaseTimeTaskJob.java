package com.elasticjob.demo;

import java.util.Map;

import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.executor.handler.JobProperties;

/**
 * 定时任务基类
 * @author wesley
 *
 */
public class BaseTimeTaskJob {
	/** 任务名称（不写默认为spring配置中的id） */
	private String                             jobName;

	/** cron表达式(必填) */
	private String                             cron;

	/** 分片总数(必填) */
	private int                                shardingTotalCount;

	/** 分片序列号和参数用等号分隔，多个键值对用逗号分隔 分片序列号从0开始，不可大于或等于作业分片总数 如： 0=a,1=b,2=c */
	private String                             shardingItemParameters;

	/** 作业自定义参数 */
	private String                             jobParameter;

	/** 是否开启失效转移 仅monitorExecution开启，失效转移才有效 */
	private boolean                            failover = false;

	/** 是否开启错过任务重新执行 */
	private boolean                            misfire  = true;

	/** 作业描述信息 */
	private String                             description;

	/** 作业定制化属性，目前支持job_exception_handler和executor_service_handler，用于扩展异常处理和自定义作业处理线程池 */
	private JobProperties                      jobProperties;

	/**  */
	private Map<String, JobEventConfiguration> jobEventConfigs;

	/** 返回 任务名称（不写默认为spring配置中的id）
	 * 
	 * @return 任务名称（不写默认为spring配置中的id） */
	public String getJobName() {
		return jobName;
	}

	/** 设置 任务名称（不写默认为spring配置中的id）
	 * 
	 * @param jobName
	 *        任务名称（不写默认为spring配置中的id） */
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	/** 返回 cron表达式
	 * 
	 * @return cron表达式 */
	public String getCron() {
		return cron;
	}

	/** 设置 cron表达式
	 * 
	 * @param cron
	 *        cron表达式 */
	public void setCron(String cron) {
		this.cron = cron;
	}

	/** 返回 分片总数
	 * 
	 * @return 分片总数 */
	public int getShardingTotalCount() {
		return shardingTotalCount;
	}

	/** 设置 分片总数
	 * 
	 * @param shardingTotalCount
	 *        分片总数 */
	public void setShardingTotalCount(int shardingTotalCount) {
		this.shardingTotalCount = shardingTotalCount;
	}

	/** 返回 分片序列号和参数用等号分隔，多个键值对用逗号分隔 分片序列号从0开始，不可大于或等于作业分片总数 如： 0=a1=b2=c
	 * 
	 * @return 分片序列号和参数用等号分隔，多个键值对用逗号分隔 分片序列号从0开始，不可大于或等于作业分片总数 如： 0=a1=b2=c */
	public String getShardingItemParameters() {
		return shardingItemParameters;
	}

	/** 设置 分片序列号和参数用等号分隔，多个键值对用逗号分隔 分片序列号从0开始，不可大于或等于作业分片总数 如： 0=a1=b2=c
	 * 
	 * @param shardingItemParameters
	 *        分片序列号和参数用等号分隔，多个键值对用逗号分隔 分片序列号从0开始，不可大于或等于作业分片总数 如： 0=a1=b2=c */
	public void setShardingItemParameters(String shardingItemParameters) {
		this.shardingItemParameters = shardingItemParameters;
	}

	/** 返回 作业自定义参数
	 * 
	 * @return 作业自定义参数 */
	public String getJobParameter() {
		return jobParameter;
	}

	/** 设置 作业自定义参数
	 * 
	 * @param jobParameter
	 *        作业自定义参数 */
	public void setJobParameter(String jobParameter) {
		this.jobParameter = jobParameter;
	}

	/** 返回 是否开启失效转移 仅monitorExecution开启，失效转移才有效
	 * 
	 * @return 是否开启失效转移 仅monitorExecution开启，失效转移才有效 */
	public boolean isFailover() {
		return failover;
	}

	/** 设置 是否开启失效转移 仅monitorExecution开启，失效转移才有效
	 * 
	 * @param failover
	 *        是否开启失效转移 仅monitorExecution开启，失效转移才有效 */
	public void setFailover(boolean failover) {
		this.failover = failover;
	}

	/** 返回 是否开启错过任务重新执行
	 * 
	 * @return 是否开启错过任务重新执行 */
	public boolean isMisfire() {
		return misfire;
	}

	/** 设置 是否开启错过任务重新执行
	 * 
	 * @param misfire
	 *        是否开启错过任务重新执行 */
	public void setMisfire(boolean misfire) {
		this.misfire = misfire;
	}

	/** 返回 作业描述信息
	 * 
	 * @return 作业描述信息 */
	public String getDescription() {
		return description;
	}

	/** 设置 作业描述信息
	 * 
	 * @param description
	 *        作业描述信息 */
	public void setDescription(String description) {
		this.description = description;
	}

	/** 返回 作业定制化属性，目前支持job_exception_handler和executor_service_handler，用于扩展异常处理和自定义作业处理线程池
	 * 
	 * @return 作业定制化属性，目前支持job_exception_handler和executor_service_handler，用于扩展异常处理和自定义作业处理线程池 */
	public JobProperties getJobProperties() {
		return jobProperties;
	}

	/** 设置 作业定制化属性，目前支持job_exception_handler和executor_service_handler，用于扩展异常处理和自定义作业处理线程池
	 * 
	 * @param jobProperties
	 *        作业定制化属性，目前支持job_exception_handler和executor_service_handler，用于扩展异常处理和自定义作业处理线程池 */
	public void setJobProperties(JobProperties jobProperties) {
		this.jobProperties = jobProperties;
	}

	/** 返回
	 * 
	 * @return */
	public Map<String, JobEventConfiguration> getJobEventConfigs() {
		return jobEventConfigs;
	}

	/** 设置
	 * 
	 * @param jobEventConfigs */
	public void setJobEventConfigs(Map<String, JobEventConfiguration> jobEventConfigs) {
		this.jobEventConfigs = jobEventConfigs;
	}

}
