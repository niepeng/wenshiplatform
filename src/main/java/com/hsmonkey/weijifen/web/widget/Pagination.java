package com.hsmonkey.weijifen.web.widget;
import wint.mvc.module.annotations.Action;
import wint.mvc.template.Context;

import com.hsmonkey.weijifen.web.common.pagination.PageFacer;
import com.hsmonkey.weijifen.web.common.pagination.PageFacerFactory;

public class Pagination {

	@Action
	public void execute(Context context) {
//		System.out.println("test !!!");
//		context.put("testDate", new Date());
		String name = (String)context.get("pageFacerName");
		PageFacer pageFacer = PageFacerFactory.getPageFacer(name, context);
		context.put("pageChangerFaced", pageFacer);
	}
	
}
