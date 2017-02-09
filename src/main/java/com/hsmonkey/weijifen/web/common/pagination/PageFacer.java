package com.hsmonkey.weijifen.web.common.pagination;

import wint.mvc.view.Render;

public interface PageFacer {
	
	Render gotoPage(int page);
	
}