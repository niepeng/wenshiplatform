package com.hsmonkey.weijifen.biz.ao.impl;

import com.hsmonkey.weijifen.biz.ao.BaseAO;
import com.hsmonkey.weijifen.biz.ao.TaskAO;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2016年4月21日  下午9:06:25</p>
 * <p>作者：niepeng</p>
 */
public class TaskAOImpl extends BaseAO implements TaskAO {
	
	/*
	 * true or false
	 * 表示当前这个服务器是否跑该任务
	 */
	public static String executer;
	
	public static String uploadFolderPath;
	
	public String getExecuter() {
		return executer;
	}

	public void setExecuter(String executer) {
		TaskAOImpl.executer = executer;
	}

	public  String getUploadFolderPath() {
		return uploadFolderPath;
	}

	public void setUploadFolderPath(String uploadFolderPath) {
		TaskAOImpl.uploadFolderPath = uploadFolderPath;
	}
	
}
