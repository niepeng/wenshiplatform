package com.hsmonkey.weijifen.quartz;
 import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @Description: 任务执行类
 *
 */
public class QuartzJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	JobDataMap  jobMap  = context.getJobDetail().getJobDataMap();
    	 System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+ "★★★★★★★★★★★");
    	if(jobMap != null) {
    		System.out.println("testKeyValue="+jobMap.getString("testKeyValue"));
    	}

    }
}