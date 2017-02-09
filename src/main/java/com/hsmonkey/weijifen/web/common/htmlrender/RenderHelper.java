package com.hsmonkey.weijifen.web.common.htmlrender;

import java.io.StringWriter;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import wint.lang.utils.CollectionUtil;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: 驭宝</p>
 * <p>创建时间: 2015-11-25  下午5:58:05</p>
 * <p>作者：niepeng</p>
 */
public class RenderHelper {

	private static final VelocityEngine ve = new VelocityEngine();
	private static final String fileprefix = "/src/main/webapp/templates/htmlrender/";

	/**
	 * 根据bean和vm文件，渲染得到结果
	 *
	 * @param bean
	 * @param vmName
	 * @return
	 */
	public static String render(RenderBean bean, String vmName) {
		try {
			// 配置引擎上下文对象
			VelocityContext ctx = new VelocityContext();
			ctx.put("renderBean", bean);

			// 加载模板文件
			Template t = ve.getTemplate(fileprefix + vmName);
			StringWriter sw = new StringWriter();

			// 渲染模板
			t.merge(ctx, sw);

			return sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据bean和父vm、子vm文件，得到渲染结果
	 *
	 * @param bean
	 * @param fatherVm
	 * @param sonVm
	 * @return
	 */
	public static String render(RenderBean bean, String fatherVm, String sonVm) {
		if (fatherVm == null || bean == null) {
			return null;
		}

		if (sonVm == null || CollectionUtil.isEmpty(bean.getSonList())) {
			return render(bean, fatherVm);
		}

		StringBuilder sonValues = new StringBuilder();
		for (RenderBean son : bean.getSonList()) {
			sonValues.append(render(son, sonVm));
		}
		bean.setSonValue(sonValues.toString());

		return render(bean, fatherVm);
	}

	/**
	 * 根据List<RenderBean>和父vm、子vm文件，得到渲染结果
	 *
	 * @param list
	 * @param fatherVm
	 * @param sonVm
	 * @return
	 */
	public static String render(List<RenderBean> list, String fatherVm, String sonVm) {
		if (CollectionUtil.isEmpty(list)) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (RenderBean bean : list) {
			sb.append(render(bean, fatherVm, sonVm));
		}
		return sb.toString();
	}

	/**
	 * 根据List<RenderBean>和vm文件，得到渲染结果
	 *
	 * @param list
	 * @param vm
	 * @return
	 */
	public static String render(List<RenderBean> list, String vm) {
		return render(list, vm, null);
	}

}
