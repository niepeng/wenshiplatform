package com.hsmonkey.weijifen.common.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2016年9月10日  上午10:39:07</p>
 * <p>作者：niepeng</p>
 */
public class InstantiationTracingBeanPostProcessor implements ApplicationListener {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	private String executer;

	private static boolean isRun = false;

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (isRun) {
			return;
		}
		isRun = true;
		ContextRefreshedEvent c = (ContextRefreshedEvent) event;
		ApplicationContext context = c.getApplicationContext();
		 if(!isMasterRun()) {
			 return;
		 }
		Object object = (Object) context.getBean("actAO");
//		if(object instanceof ActAO) {
//			final ActAO actAO = (ActAO) object;
//			new Thread(new Runnable() {
//				@Override
//				public void run() {
//					actAO.repairSimilarData();
//				}
//			}).start();
//		}
	}
	
	private boolean isMasterRun() {
		return "true".equals(executer);
	}

	public String getExecuter() {
		return executer;
	}

	public void setExecuter(String executer) {
		this.executer = executer;
	}
	
}
