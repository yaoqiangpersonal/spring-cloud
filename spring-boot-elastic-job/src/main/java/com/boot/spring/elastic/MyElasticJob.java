package com.boot.spring.elastic;

import org.springframework.beans.factory.annotation.Autowired;

import com.boot.spring.po.Animal;
import com.boot.spring.po.Person;
import com.cxytiandi.elasticjob.annotation.ElasticJobConf;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

@ElasticJobConf(
		name = "job1", 
		cron = "0/10 * * * * ?", 
		shardingItemParameters = "0=test1,1=test2", 
		description = "简单任务",
		shardingTotalCount = 2,
		jobShardingStrategyClass = "com.dangdang.ddframe.job.lite.api.strategy.impl.AverageAllocationJobShardingStrategy"
		)
public class MyElasticJob implements SimpleJob {

	//@Autowired
	//private Animal person;
	
	@Autowired
	private Person person;
	
	@Override
	public void execute(ShardingContext shardingContext) {
		System.out.println(person.getClass());
		System.out.println(shardingContext);
		
	}

}
