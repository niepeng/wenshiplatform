package com.hsmonkey.weijifen.web.common.pagination;

import wint.mvc.template.Context;

public abstract class BasePageFacer implements PageFacer {

	protected Context getTemplateContext() {
		return PageFacerFactory.getTemplateContext();
	}

}