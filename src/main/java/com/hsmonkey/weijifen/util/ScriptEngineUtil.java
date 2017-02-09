package com.hsmonkey.weijifen.util;

import java.io.File;
import java.io.FileReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2016年1月8日  下午4:36:30</p>
 * <p>作者：niepeng</p>
 */
public class ScriptEngineUtil {
	
	private static Invocable invocable;
	
	/**
	 * "8ee0e9eff1851c87277f83fc3adb2b0a&1452240857692&12574478&{\"orderType\":\"sales\",\"offset\":300,\"limit\":50}"
	 */
	public static String taoEncryption(String s) {
		if (invocable == null) {
			try {
				ScriptEngineManager manager = new ScriptEngineManager();
				ScriptEngine engine = manager.getEngineByName("javascript");
				// 读取js文件
				String jsFileName = "/Users/lsb/data/code/yubao/qiangtao/src/main/webapp/js/taofunction.js";
				File file = new File(jsFileName);
				if(!file.exists()) {
					jsFileName = "/home/admin/qiangtao/work/src/main/webapp/js/taofunction.js";
				}
				FileReader reader = new FileReader(jsFileName);
				engine.eval(reader);

				if (engine instanceof Invocable) {
					invocable = (Invocable) engine;
				}
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (invocable == null) {
			return null;
		}

		try {
			// c = n(xxxx); 调用n函数
			return (String) invocable.invokeFunction("n", s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
