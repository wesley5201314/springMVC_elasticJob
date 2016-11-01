package com.elasticjob.demo;

import com.dangdang.ddframe.job.api.dataflow.DataflowJob;

/**
 * 流式定时任务抽象类
 * @author wesley
 *
 * @param <T>
 */
public abstract class AbstractDataflowTimeTaskJob<T> extends BaseTimeTaskJob implements DataflowJob<T> {

}
