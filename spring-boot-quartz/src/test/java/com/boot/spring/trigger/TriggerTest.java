package com.boot.spring.trigger;

import java.util.concurrent.TimeUnit;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

import lombok.Setter;

public class TriggerTest {
	
	public static void main(String[] args) throws Exception {
		SchedulerFactory factory = new StdSchedulerFactory();
		Scheduler scheduler = factory.getScheduler();
		scheduler.start();
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity(new TriggerKey("test", "test"))
				.withSchedule(CronScheduleBuilder.cronSchedule("0/2 * * ? * *"))
				.build();
		JobDetail jboDetail = JobBuilder.newJob(myJob.class)
				.withIdentity(new JobKey("test", "test"))
				.usingJobData("name", "xiaogao")
				.build();
		scheduler.scheduleJob(jboDetail, trigger);
		
		TimeUnit.SECONDS.sleep(10);
		scheduler.shutdown();
	}
	
	public static class myJob implements Job {
		
		@Setter
		private String name;
		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {
			String s = context.getMergedJobDataMap().getString("user");
			System.out.println(name);
			
		}
		
	}

}


