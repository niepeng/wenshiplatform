package com.hsmonkey.weijifen.web.common.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.hsmonkey.weijifen.biz.bean.DeviceBean;
import com.hsmonkey.weijifen.biz.bean.DeviceDataBean;
import com.hsmonkey.weijifen.util.CollectionUtils;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2017年2月17日  下午1:14:15</p>
 * <p>作者：niepeng</p>
 */
public class ExcelUtil {
	
	/*
	 * 生成excel文件，格式如下：
	 * 
	 * 成前科技云平台——历史数据
	 * 
	 * 
	 * NO 记录时间 区域名-名称 区域名-名称
	 * 			  温度(℃)   湿度(%RH)
	 * 1 2014-09-18 19:41:00 26.62 0.00
	 * 2 2014-09-18 19:42:00 26.62 0.00
	 * 3 2014-09-18 19:43:00 26.62 0.00
	 * .......
	 */
	@SuppressWarnings("deprecation")
	public static HSSFWorkbook genExcel(DeviceBean deviceBean) {
		// 第一步，创建一个webbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet("导出数据");
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
		HSSFRow row1 = sheet.createRow((int) 0);
		sheet.setDefaultColumnWidth(36);

		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

		HSSFCell cell = row1.createCell((short) 0);
		cell.setCellValue("成前云平台――历史数据");
		cell.setCellStyle(style);
		cell = row1.createCell((short) 1);
		// cell.setCellValue(String.valueOf(address));
		// cell.setCellStyle(style);

		HSSFRow row2 = sheet.createRow((int) 1);
		HSSFCell cell2 = row2.createCell((short) 0);
		cell2.setCellValue("");
		cell2.setCellStyle(style);
		cell2 = row2.createCell((short) 1);

		HSSFRow row3 = sheet.createRow((int) 2);
		HSSFCell cell3 = row3.createCell((short) 0);
		cell3.setCellValue("");
		cell3.setCellStyle(style);

		HSSFRow row4 = sheet.createRow((int) 3);
		HSSFCell cell4 = row4.createCell((short) 0);
		cell4.setCellValue("NO");
		cell4.setCellStyle(style);
		cell4 = row4.createCell((short) 1);
		cell4.setCellValue("记录时间");
		cell4.setCellStyle(style);

		// 添加一个设备
		int titleCellNum = 2;
		cell4 = row4.createCell((short) titleCellNum++);
		cell4.setCellValue(deviceBean.getShowValue());
		cell4.setCellStyle(style);
		cell4 = row4.createCell((short) titleCellNum++);
		cell4.setCellValue(deviceBean.getShowValue());
		cell4.setCellStyle(style);

		HSSFRow row5 = sheet.createRow((int) 4);
		HSSFCell cell5 = row5.createCell((short) 0);
		cell5.setCellValue("");
		cell5.setCellStyle(style);
		cell5 = row5.createCell((short) 1);
		cell5.setCellValue("");
		cell5.setCellStyle(style);
		titleCellNum = 2;

		cell5 = row5.createCell((short) titleCellNum++);
		cell5.setCellValue("温度(℃)");
		cell5.setCellStyle(style);
		cell5 = row5.createCell((short) titleCellNum++);
		cell5.setCellValue("湿度(%RH)");
		cell5.setCellStyle(style);

		int rowIndex = 5;
		int startCell = 0;
		if (deviceBean.getDeviceDataBeanList() != null) {
			for (int k = 0, size = deviceBean.getDeviceDataBeanList().size(); k < size; k++) {
				DeviceDataBean bean = deviceBean.getDeviceDataBeanList().get(k);
				HSSFRow tmpRow = sheet.createRow((int) rowIndex++);
				HSSFCell tmpCell = tmpRow.createCell((short) 0);
				tmpCell.setCellValue(String.valueOf(k + 1));
				tmpCell.setCellStyle(style);
				tmpCell = tmpRow.createCell((short) 1);
				tmpCell.setCellValue(bean.getTime());
				tmpCell.setCellStyle(style);
				startCell = 2;
				tmpCell = tmpRow.createCell((short) startCell++);
				tmpCell.setCellValue(bean.getTemp());
				tmpCell.setCellStyle(style);
				tmpCell = tmpRow.createCell((short) startCell++);
				tmpCell.setCellValue(bean.getHumi());
				tmpCell.setCellStyle(style);
			}
		}
		return wb;
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
		
		File file = new File("b.xls");
		System.out.println(file.getAbsolutePath());
		HSSFWorkbook wb = genExcel(bean);
		try {
			wb.write(new FileOutputStream(file));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
