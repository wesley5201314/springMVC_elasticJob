package com.elasticjob.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.dangdang.ddframe.job.api.ShardingContext;

public class MyDataflowTimeTaskJob extends AbstractDataflowTimeTaskJob<String> {

	private static AtomicInteger count = new AtomicInteger(0);
	
	private static List<String> allData = new ArrayList<String>();
	
	static{
		for (int i = 0; i < 10; i++) {
			allData.add(i+"");
        }
	}

	@Override
	public List<String> fetchData(ShardingContext shardingContext) {
		System.out.println("--------start--------");
		List<String> data = new ArrayList<String>();
		int i = shardingContext.getShardingItem();
		if(i<allData.size() && allData.get(i)!=null){
			data.add(allData.get(i));
		}
		return data;
	}

	@Override
	public void processData(ShardingContext shardingContext, List<String> data) {
		System.out.println("总分片=" + shardingContext.getShardingTotalCount());
		System.out.println("当前的数据=" + data);
		System.out.println("第" + count.addAndGet(1) + "次执行，当前分片号为：" + shardingContext.getShardingItem());
		try {
	        Thread.sleep(60000);
        }
        catch (InterruptedException e) {
	        e.printStackTrace();
        }
		allData.removeAll(data);
		System.out.println("one end!");
	}
}
