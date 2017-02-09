package com.hsmonkey.weijifen.web.common.pagination;

import wint.core.service.ServiceContext;
import wint.mvc.holder.WintContext;
import wint.mvc.url.UrlBroker;
import wint.mvc.url.UrlBrokerService;
import wint.mvc.view.Render;

public class AccountListPageFacer extends BasePageFacer{

	@Override
	public Render gotoPage(int page) {
		ServiceContext context = WintContext.getServiceContext();
		UrlBrokerService urlBrokerService = (UrlBrokerService) context.getObject("urlBrokerService");
		UrlBroker broker = urlBrokerService.makeUrlBroker("adminModule", "account/mainList");

//		UserQuery query = (UserQuery) getTemplateContext().get("query");
//		broker.param("pageSize", query.getPageSize());
//		broker.param("page", page);
//		broker.param("queryCondition", query.getQueryCondition());
		return broker;
	}

}
