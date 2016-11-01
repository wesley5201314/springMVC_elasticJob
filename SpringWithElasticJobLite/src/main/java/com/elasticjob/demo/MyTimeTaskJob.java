package com.elasticjob.demo;

import java.util.concurrent.atomic.AtomicInteger;

import com.dangdang.ddframe.job.api.ShardingContext;

/**
 * 作业任务
 * @author wesley
 *
 */
public class MyTimeTaskJob extends AbstractSimpleTimeTaskJob{

	private static AtomicInteger count = new AtomicInteger(0);
	
	@Override
	public void execute(ShardingContext shardingContext) {
		System.out.println(shardingContext.getJobParameter());
		System.out.println("MyTimeTaskJob总分片=" + shardingContext.getShardingTotalCount());
		System.out.println("MyTimeTaskJob当前分片=" + shardingContext.getShardingItem());
		System.out.println("MyTimeTaskJob第" + count.addAndGet(1) + "次执行，当前分片号为：" + shardingContext.getShardingParameter());
	}

}
