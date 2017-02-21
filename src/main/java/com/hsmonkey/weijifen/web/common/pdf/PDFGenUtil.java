package com.hsmonkey.weijifen.web.common.pdf;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hsmonkey.weijifen.biz.bean.DeviceBean;
import com.hsmonkey.weijifen.biz.bean.DeviceDataBean;
import com.hsmonkey.weijifen.util.CollectionUtils;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2017年2月17日  下午1:11:03</p>
 * <p>作者：niepeng</p>
 */
public class PDFGenUtil {
	
	protected final static Logger log = LoggerFactory.getLogger(PDFGenUtil.class);

	private static Font headfont;// 设置字体大小
	private static Font keyfont;// 设置字体大小
	private static Font textfont;// 设置字体大小

	static {
		BaseFont bfChinese;
		try {
			// bfChinese =
			// BaseFont.createFont("STSong-Light","UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
			bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			headfont = new Font(bfChinese, 10, Font.BOLD);// 设置字体大小
			keyfont = new Font(bfChinese, 8, Font.BOLD);// 设置字体大小
			textfont = new Font(bfChinese, 8, Font.NORMAL);// 设置字体大小
		} catch (Exception e) {
			log.error("static error", e);
		}
	}
	
	public static void main(String[] args) {
		
		
		DeviceBean bean = new DeviceBean();
		bean.setArea("区域w");
		bean.setDevName("我是name");
		
		List<DeviceDataBean> dataList = CollectionUtils.newArrayList();
		DeviceDataBean dataBean1 = new DeviceDataBean();
		dataBean1.setTemp("12.21");
		dataBean1.setHumi("78.21");
		dataBean1.setTime("2016-01-01 12:00:01");
		
		DeviceDataBean dataBean2 = new DeviceDataBean();
		dataBean2.setTemp("12.22");
		dataBean2.setHumi("78.22");
		dataBean2.setTime("2016-01-01 12:00:02");
		
		dataList.add(dataBean1);
		dataList.add(dataBean2);
		bean.setDeviceDataBeanList(dataList);
		
		try {
			File file = new File("abd1.pdf");
			file.createNewFile();
			OutputStream out = new FileOutputStream(file);
			genPdf(bean, out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/*
	 * 生成pdf文件，格式如下：
	 * 
	 * 成前云平台
	 * 
	 * 时间间隔:xx分钟	  时间段：  查询开始时间xx   查询结束时间xxx
	 * 		区域设备名称
	 * NO 时间	温度(℃)   湿度(%RH)
	 * 1 2014-09-18 19:41:00 26.62 0.00
	 * 2 2014-09-18 19:42:00 26.62 0.00
	 * 3 2014-09-18 19:43:00 26.62 0.00
	 * .......
	 */
	public static void genPdf(DeviceBean deviceBean, OutputStream out) {
		try {
			Document document = new Document();
			document.setPageSize(PageSize.A4);// 设置页面大小
			PdfWriter.getInstance(document, out);
			document.open();
			
			PdfPTable table = createTable(4);
			table.addCell(createCell("成前云平台", keyfont, Element.ALIGN_CENTER, 4, true));

			table.addCell(createCell("时间间隔:"+deviceBean.getDataBean().getRangeTime()+"分钟", keyfont, Element.ALIGN_CENTER));
			table.addCell(createCell("时间段：", keyfont, Element.ALIGN_CENTER));
			table.addCell(createCell(deviceBean.getDataBean().getStartTime(), keyfont, Element.ALIGN_CENTER));
			table.addCell(createCell(deviceBean.getDataBean().getEndTime(), keyfont, Element.ALIGN_CENTER));
			
			table.addCell(createCell(deviceBean.getShowValue(), keyfont, Element.ALIGN_CENTER, 4, true));

			table.addCell(createCell("NO", keyfont, Element.ALIGN_CENTER));
			table.addCell(createCell("时间", keyfont, Element.ALIGN_CENTER));
			table.addCell(createCell("温度(℃)", keyfont, Element.ALIGN_CENTER));
			table.addCell(createCell("湿度(%RH)", keyfont, Element.ALIGN_CENTER));

			List<DeviceDataBean> dataList = deviceBean.getDeviceDataBeanList();
			if (dataList != null) {
				for (int i = 0, size = dataList.size(); i < size; i++) {
					table.addCell(createCell(String.valueOf(i + 1), textfont));
					table.addCell(createCell(dataList.get(i).getTime(), textfont));
					table.addCell(createCell(dataList.get(i).getTemp(), textfont));
					table.addCell(createCell(dataList.get(i).getHumi(), textfont));
				}
			}
			
			document.add(table);
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("genPdfDocumentError", e);
		}
	}
	
	static int maxWidth = 520;
	private static PdfPTable createTable(int colNumber) {
		PdfPTable table = new PdfPTable(colNumber);
		try {
			table.setTotalWidth(maxWidth);
			table.setLockedWidth(true);
			table.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.getDefaultCell().setBorder(1);
		} catch (Exception e) {
			log.error("createTable error", e);
		}
		return table;
	}
	
	
	private static PdfPCell createCell(String value, com.lowagie.text.Font font, int align, int colspan, boolean boderFlag) {
		PdfPCell cell = new PdfPCell();
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(align);
		cell.setColspan(colspan);
		cell.setPhrase(new Phrase(value, font));
		cell.setPadding(3.0f);
		if (!boderFlag) {
			cell.setBorder(0);
			cell.setPaddingTop(15.0f);
			cell.setPaddingBottom(8.0f);
		}
		return cell;
	}
	
	private static PdfPCell createCell(String value, com.lowagie.text.Font font, int align) {
		PdfPCell cell = new PdfPCell();
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(align);
		cell.setPhrase(new Phrase(value, font));
		return cell;
	}
	
	private static PdfPCell createCell(String value, com.lowagie.text.Font font) {
		PdfPCell cell = new PdfPCell();
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setPhrase(new Phrase(value, font));
		return cell;
	}
	
	
}
