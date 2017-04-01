package com.hsmonkey.weijifen.web.action;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wint.help.biz.result.Result;
import wint.help.biz.result.ResultCode;
import wint.help.biz.result.results.CommonResultCodes;
import wint.lang.utils.StringUtil;
import wint.mvc.flow.FlowData;
import wint.mvc.flow.ServletFlowData;
import wint.mvc.flow.Session;
import wint.mvc.template.Context;

import com.hsmonkey.weijifen.biz.bean.DeviceBean;
import com.hsmonkey.weijifen.biz.bean.UserBean;
import com.hsmonkey.weijifen.common.SessionKeys;
import com.hsmonkey.weijifen.common.http.HttpClient;
import com.hsmonkey.weijifen.web.common.pdf.PDFGenUtil;

/**
 * @author niepeng
 *
 */
public class BaseAction {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	public static final String JSON_TYPE = "application/json;charset=UTF-8";

	
	protected static HttpClient client = new HttpClient(false);
	
//	protected static final String API_URL = "http://42.121.53.218:2500";
//	protected static final String API_URL = "http://api.eefield.com:2500";
	protected static final String API_URL = "http://api.eefield.com/";
	
	//电子邮件    
	 private static final String EMAIL_PATTEN = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";    
	 public static Pattern emailPatten = Pattern.compile(EMAIL_PATTEN);

	protected boolean checkUserSession(FlowData flowData, Context context) {
		Session session = flowData.getSession();
		String userName = (String) session.getAttribute(SessionKeys.USER_NAME);
		if (StringUtil.isBlank(userName)) {
			return false;
		}
		return true;
	}
	
	protected UserBean getUserBean(FlowData flowData) {
		UserBean userBean = new UserBean();
		Session session = flowData.getSession();
		userBean.setUser((String) session.getAttribute(SessionKeys.USER_NAME));
		if (StringUtil.isBlank(userBean.getUser())) {
			return null;
		}
		return userBean;
	}
	
	

	protected boolean checkUserSessionNeedRedrect(FlowData flowData, Context context) {
		boolean isSuccess = checkUserSession(flowData, context);
		if (!isSuccess) {
			flowData.redirectTo("userModule", "login");
			return false;
		}
		return true;
	}

	protected void handleResult(Result result, FlowData flowData, Context context) {
		if(!result.isSuccess()) {
		    result2Context(result, context);
			handleError(result, flowData, context);
			return;
		}
		result2Context(result, context);
	}
	
	protected void handleApiResult(Result result, FlowData flowData, Context context) {
		flowData.setContentType(JSON_TYPE);
		if(!result.isSuccess()) {
			handleJsonApiError(result, flowData, context);
			return;
		}
		result2Context(result, context);
	}
	
	protected void handleJsonApiError(Result result, FlowData flowData, Context context) {
		ResultCode resultCode = result.getResultCode();
		if (resultCode == null) {
			resultCode = CommonResultCodes.SYSTEM_ERROR;
		}
		context.put("code", resultCode.getCode());
		context.put("errorMessage", resultCode.getMessage());
		flowData.forwardTo("/json/apierror");
	}
	
	protected void handleJsonResult(Result result, FlowData flowData, Context context) {
		flowData.setContentType(JSON_TYPE);
		if(!result.isSuccess()) {
			handleJsonError(result, flowData, context);
			return;
		}
		result2Context(result, context);
	}
	
	protected void handleJsonError(Result result, FlowData flowData, Context context) {
		ResultCode resultCode = result.getResultCode();
		if (resultCode == null) {
			resultCode = CommonResultCodes.SYSTEM_ERROR;
		}
		context.put("code", resultCode.getCode());
		context.put("errorMessage", resultCode.getMessage());
		flowData.forwardTo("/json/error");
	}

	protected void handleError(Result result, FlowData flowData, Context context) {
		ResultCode resultCode = result.getResultCode();

		if (resultCode == null) {
			resultCode = CommonResultCodes.SYSTEM_ERROR;
		}
//		else if (AdminResultCodes.USER_NOT_LOGIN == resultCode) {
//			flowData.redirectTo("adminModule", "login");
//			return;
//		}
		flowData.redirectTo("baseModule", "message").param("msg", urlEncode(resultCode.getMessage()));
	}
	
	 protected String urlEncode(String msg) {
			try {
				return URLEncoder.encode(msg, "utf-8");
			} catch (Exception e) {
			}
			return null;
		}

	    protected String urlDecode(String msg) {
			if (msg == null) {
				return null;
			}

			try {
				return URLDecoder.decode(msg, "utf-8");
			} catch (Exception e) {
			}
			return null;
		}


	protected void result2Context(Result result, Context context) {
		for (Map.Entry<String, Object> entry : result.getModels().entrySet()) {
			context.put(entry.getKey(), entry.getValue());
		}
		ResultCode resultCode = result.getResultCode();
		// 这里可能是上一个页面重定向过来的，保留错误信息，如：修改操作失败后返回到编辑页面
		if(resultCode != null) {
			context.put("resultmessage", resultCode);
		}
	}
	
	protected void handleExcel(FlowData flowData, Result result, HSSFWorkbook wb, String fileName, String charset) {
		ServletOutputStream out = null;
		try {
			ServletFlowData servletFlowData = (ServletFlowData) flowData;
			flowData.setViewType("nop");

			final String userAgent = servletFlowData.getRequest().getHeader("USER-AGENT");
			// 处理各个浏览器乱码问题
			try {
				if (userAgent.indexOf("MSIE") >= 0) {// IE浏览器
					fileName = URLEncoder.encode(fileName, "UTF-8");
				} else if (userAgent.indexOf("Firefox") >= 0) {
					fileName = fileName.replaceAll(" ", "+");
					fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
				} else if (userAgent.indexOf("Mozilla") >= 0) {
					fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
				} else {
					fileName = URLEncoder.encode(fileName, "UTF-8");// 其他浏览器
				}

			} catch (Exception e) {
			}

			HttpServletResponse response = servletFlowData.getResponse();
			response.reset();
			// response.setContentType("application/octet-stream;charset=" +
			// charset);
			response.setContentType("application/msexcel;charset=" + charset);
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			// response.setHeader("Content-Description","abc.xls open or download");

			out = response.getOutputStream();
			wb.write(out);
			out.flush();
		} catch (Exception e) {
			// log.error("export excel error", e);
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				// log.error("export excel close out error", e);
			}
		}
	}
	
	protected void handlePdf(FlowData flowData, Result result, DeviceBean deviceBean, String fileName, String charset) {
		ServletOutputStream out = null;
		try {
			ServletFlowData servletFlowData = (ServletFlowData) flowData;
			flowData.setViewType("nop");
			
			final String userAgent = servletFlowData.getRequest().getHeader("USER-AGENT");
			// 处理各个浏览器乱码问题
			try {
				if (userAgent.indexOf("MSIE") >= 0) {// IE浏览器
					fileName = URLEncoder.encode(fileName, "UTF-8");
				} else if (userAgent.indexOf("Firefox") >= 0) {
					fileName = fileName.replaceAll(" ", "+");
					fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
				} else if (userAgent.indexOf("Mozilla") >= 0) {
					fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
				} else {
					fileName = URLEncoder.encode(fileName, "UTF-8");// 其他浏览器
				}
				
			} catch (Exception e) {
			}
			
			HttpServletResponse response = servletFlowData.getResponse();
			response.reset();
			// response.setContentType("application/octet-stream;charset=" +
			// charset);
			response.setContentType("application/pdf;charset=" + charset);
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			// response.setHeader("Content-Description","abc.xls open or download");
			
			out = response.getOutputStream();
			PDFGenUtil.genPdf(deviceBean, out);
			out.flush();
		} catch (Exception e) {
			// log.error("export excel error", e);
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				// log.error("export excel close out error", e);
			}
		}
	}
	

}