package com.hsmonkey.weijifen.web.common.pagination;

import java.util.HashMap;
import java.util.Map;

import wint.mvc.template.Context;

/**
 * @author niepeng
 *
 * @date 2012-9-5 下午11:55:18
 */
public class PageFacerFactory {

	private static Map<String, PageFacer> pageFacers = new HashMap<String, PageFacer>();

	private static ThreadLocal<Context> targetTemplateContext = new ThreadLocal<Context>();

	static {
		pageFacers.put("accountListPageFacer", new AccountListPageFacer());
	}

	public static void clearTemplateContext() {
		targetTemplateContext.remove();
	}

	public static Context getTemplateContext() {
		return targetTemplateContext.get();
	}

	public static PageFacer getPageFacer(String name, Context context) {
		targetTemplateContext.set(context);
		return pageFacers.get(name);
	}

}