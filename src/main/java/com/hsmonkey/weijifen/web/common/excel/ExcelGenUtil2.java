//package com.hsmonkey.weijifen.web.common.excel;
//
///**
// * <p>标题: </p>
// * <p>描述: </p>
// * <p>版权: lsb</p>
// * <p>创建时间: 2017年2月17日  下午1:10:50</p>
// * <p>作者：niepeng</p>
// */
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Date;
//import java.util.List;
//
//import org.apache.poi.hssf.usermodel.HSSFCell;
//import org.apache.poi.hssf.usermodel.HSSFCellStyle;
//import org.apache.poi.hssf.usermodel.HSSFDateUtil;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//
//import wint.lang.utils.CollectionUtil;
//
//import com.hsmonkey.weijifen.util.ChangeUtil;
//import com.hsmonkey.weijifen.util.DateUtil;
//
//public class ExcelGenUtil2 {
//	
//	
//	public static void main(String[] args) {
////		File file = new File("E:/20150810/export-111.xls");
////		List<Record> list = readFromFile(file);
////		System.out.println("size=" + list.size());
//	}
//	
//	public static void main3(String[] args) {
//		try {
////			List<EquipData> equipList = CollectionUtil.newArrayList();
////			EquipData equipData1 = new EquipData();
////			equipData1.setEquipmentId(1);
////			equipData1.setPlaceStr("未定义");
////			equipData1.setMark("243");
////			
////			EquipData equipData2 = new EquipData();
////			equipData2.setEquipmentId(1);
////			equipData2.setPlaceStr("未定义2");
////			equipData2.setMark("2432");
////			
////			equipList.add(equipData1);
////			equipList.add(equipData2);
////
////			
////
////			List<EquipMoreHistoryDataBean> beanList = CollectionUtil.newArrayList();
////			EquipMoreHistoryDataBean bean1 = new EquipMoreHistoryDataBean();
////			bean1.setRecordDate("2016-11-30 20:00:00");
////			BeanForRecord record1 = new BeanForRecord();
////			record1.setEquipmentId(1);
////			record1.setTemperature("12.23");
////			record1.setHumidity("57.33");
////			BeanForRecord[] datas = new BeanForRecord[2];
////			datas[0] = record1;
////			bean1.setDatas(datas);
////
////			beanList.add(bean1);
////
////			genExcelInputStream(beanList, equipList);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//	
//	public static void main1(String[] args) {
//		try {
////			List<EquipHistoryDataBean> beanList = CollectionUtil.newArrayList();
////			EquipHistoryDataBean bean1 = new EquipHistoryDataBean();
////			bean1.setRecordDate(new Date());
////			bean1.setHumi(1231);
////			bean1.setTemp(3421);
////			
////			EquipHistoryDataBean bean2 = new EquipHistoryDataBean();
////			bean2.setRecordDate(new Date());
////			bean2.setHumi(1232);
////			bean2.setTemp(3422);
////			
////			EquipHistoryDataBean bean3 = new EquipHistoryDataBean();
////			bean3.setRecordDate(new Date());
////			bean3.setHumi(1233);
////			bean3.setTemp(3423);
////			
////			beanList.add(bean1);
////			beanList.add(bean2);
////			beanList.add(bean3);
////			
////			genExcelFile("E:/20150610", beanList, 1, "testpdfquto");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}  
//		   
//	}
//	
//	private static String findPlaceStr(List<String> equPlaceStrList, int index) {
//		if (equPlaceStrList.size() > index) {
//			return equPlaceStrList.get(index);
//		}
//		return null;
//	}
//
//	private static Object getValue(HSSFCell cell) {
//		if(cell == null) {
//			return null;
//		}
//		switch (cell.getCellType()) {
//		case HSSFCell.CELL_TYPE_STRING:
//			return cell.getStringCellValue();
//		case HSSFCell.CELL_TYPE_NUMERIC:
//			if (HSSFDateUtil.isCellDateFormatted(cell)) {
//				return cell.getDateCellValue();
//			}
//			return String.valueOf(cell.getNumericCellValue());
//		}
//		return null;
//	}
//	
//	public static String getStringValue(HSSFCell cell) {
//		Object obj = getValue(cell);
//		if (obj != null && obj instanceof String) {
//			return (String) obj;
//		}
//		return null;
//	}
//	
//	public static Date getDateValue(HSSFCell cell) {
//		Object obj = getValue(cell);
//		if (obj != null && obj instanceof Date) {
//			return (Date) obj;
//		}
//		return null;
//	}
//	
//
//	private static List<String> parseEquipList(HSSFRow titleRow) {
//		int totalColumnIndex = titleRow.getLastCellNum();
//		if (totalColumnIndex < 3) {
//			return null;
//		}
//
//		String tmp = null;
//		List<String> result = CollectionUtil.newArrayList();
//		for (int i = 2; i < totalColumnIndex; i += 2) {
//			tmp = getStringValue(titleRow.getCell(i));
//			if (tmp != null) {
//				result.add(tmp);
//			}
//		}
//		return result;
//	}
//	
//	
//	/*
//	 * 生成excel文件，格式如下：
//	 * 
//	 * 仪器编号   004
//	 * 仪器名	单温度测试区-4-单温度加盖
//	 * 时间	温度(℃)   湿度(%RH)
//	 * 2014-09-18 19:41:00 26.62 0.00
//	 * 2014-09-18 19:42:00 26.62 0.00
//	 * 2014-09-18 19:43:00 26.62 0.00
//	 * .......
//	 */
//	public static void genExcelFile(String destFileName_header, List<EquipHistoryDataBean> beanList, int address, String showName) {
//		String destFileName = destFileName_header + "/" + showName + ".xls";
//		File file = new File(destFileName);
//		try {
//			file.createNewFile();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//		
//		// 第一步，创建一个webbook，对应一个Excel文件  
//	      HSSFWorkbook wb = new HSSFWorkbook();  
//	      // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
//	      HSSFSheet sheet = wb.createSheet("导出数据");  
//	      // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
//	      HSSFRow row1 = sheet.createRow((int) 0);  
//	      sheet.setDefaultColumnWidth(36);  
//	     
//	      // 第四步，创建单元格，并设置值表头 设置表头居中  
//	      HSSFCellStyle style = wb.createCellStyle();  
//	      style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
//	      
//	      
//
//	      HSSFCell cell = row1.createCell((short) 0);  
//	      cell.setCellValue("仪器地址");  
//	      cell.setCellStyle(style);  
//	      cell = row1.createCell((short) 1);  
//	      cell.setCellValue(String.valueOf(address));  
//	      cell.setCellStyle(style);  
//	      
//	      HSSFRow row2 = sheet.createRow((int) 1);  
//	      HSSFCell cell2 = row2.createCell((short) 0); 
//	      cell2.setCellValue("仪器名");  
//	      cell2.setCellStyle(style);  
//	      cell2 = row2.createCell((short) 1);  
//	      cell2.setCellValue(showName);  
//	      cell2.setCellStyle(style);  
//	      
//	      HSSFRow row3 = sheet.createRow((int) 2);  
//	      HSSFCell cell3 = row3.createCell((short) 0); 
//	      cell3.setCellValue("报表时间");  
//	      cell3.setCellStyle(style);  
//	      cell3 = row3.createCell((short) 1);  
//	      cell3.setCellValue(DateUtil.formatFullDate(new Date()));  
//	      cell3.setCellStyle(style);  
//	      
//	      
//	      HSSFRow row4 = sheet.createRow((int) 3);  
//	      HSSFCell cell4 = row4.createCell((short) 0); 
//	      cell4.setCellValue("时间");  
//	      cell4.setCellStyle(style);  
//	      cell4 = row4.createCell((short) 1);  
//	      cell4.setCellValue("温度( ℃ )");  
//	      cell4.setCellStyle(style);  
//	      cell4 = row4.createCell((short) 2);  
//	      cell4.setCellValue("湿度(%RH)");  
//	      cell4.setCellStyle(style);  
//	      
//	      int rowIndex = 4;
//			if (beanList != null) {
//				for (int i = 0, size = beanList.size(); i < size; i++) {
//					EquipHistoryDataBean bean = beanList.get(i);
//					HSSFRow tmpRow = sheet.createRow((int) rowIndex++);  
//					HSSFCell tmpCell = tmpRow.createCell((short) 0); 
//					tmpCell.setCellValue(DateUtil.formatDateNoException(bean.getRecordDate()));  
//					tmpCell.setCellStyle(style); 
//					
//					tmpCell = tmpRow.createCell((short) 1); 
//					tmpCell.setCellValue(ChangeUtil.chu100(bean.getTemp()));  
//					tmpCell.setCellStyle(style); 
//					
//					tmpCell = tmpRow.createCell((short) 2); 
//					tmpCell.setCellValue(ChangeUtil.chu100(bean.getHumi()));  
//					tmpCell.setCellStyle(style); 
//					  
//				}
//			}
//	      
//	      try {
//				wb.write(new FileOutputStream(file));
//			} catch (Exception e) {
//				e.printStackTrace();
//			}  
//	}
//	
//	
//
//	
//	/*
//	 * 生成excel文件，格式如下：
//	 * 
//	 * 仪器编号   004
//	 * 仪器名	单温度测试区-4-单温度加盖
//	 * 时间	温度(℃)   湿度(%RH)
//	 * 2014-09-18 19:41:00 26.62 0.00
//	 * 2014-09-18 19:42:00 26.62 0.00
//	 * 2014-09-18 19:43:00 26.62 0.00
//	 * .......
//	 */
//	public static InputStream genExcelInputStream(List<EquipMoreHistoryDataBean> beanList, List<EquipData> equipList) {
//		// 第一步，创建一个webbook，对应一个Excel文件  
//	      HSSFWorkbook wb = new HSSFWorkbook();  
//	      // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
//	      HSSFSheet sheet = wb.createSheet("导出数据");  
//	      // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
//	      HSSFRow row1 = sheet.createRow((int) 0);  
//	      sheet.setDefaultColumnWidth(36);  
//	     
//	      // 第四步，创建单元格，并设置值表头 设置表头居中  
//	      HSSFCellStyle style = wb.createCellStyle();  
//	      style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
//	      
//	      
//
//	      HSSFCell cell = row1.createCell((short) 0);  
//	      cell.setCellValue("温湿度监控平台――历史数据");  
//	      cell.setCellStyle(style);  
//	      cell = row1.createCell((short) 1);  
////	      cell.setCellValue(String.valueOf(address));  
////	      cell.setCellStyle(style);  
//	      
//	      HSSFRow row2 = sheet.createRow((int) 1);  
//	      HSSFCell cell2 = row2.createCell((short) 0); 
//	      cell2.setCellValue("");  
//	      cell2.setCellStyle(style);  
//	      cell2 = row2.createCell((short) 1);  
//	      
//	      HSSFRow row3 = sheet.createRow((int) 2);  
//	      HSSFCell cell3 = row3.createCell((short) 0); 
//	      cell3.setCellValue("");  
//	      cell3.setCellStyle(style);  
//	      
//	      HSSFRow row4 = sheet.createRow((int) 3);  
//	      HSSFCell cell4 = row4.createCell((short) 0); 
//	      cell4.setCellValue("NO");  
//	      cell4.setCellStyle(style);  
//	      cell4 = row4.createCell((short) 1);  
//	      cell4.setCellValue("记录时间");  
//	      cell4.setCellStyle(style);  
//	      
//	      // 添加一个设备
//	      int titleCellNum = 2;
//	      
//	      EquipData equipData = null;
//		  for (int s = 0, size = equipList.size(); s < size; s++) {
//			  equipData = equipList.get(s);
//			  cell4 = row4.createCell((short) titleCellNum++);  
//		      cell4.setCellValue(equipData.getShowValue());  
//		      cell4.setCellStyle(style);  
//		      cell4 = row4.createCell((short) titleCellNum++);  
//		      cell4.setCellValue(equipData.getShowValue());  
//		      cell4.setCellStyle(style);
//		  }
//	      
//	      
//	      
//	      HSSFRow row5 = sheet.createRow((int) 4);  
//	      HSSFCell cell5 = row5.createCell((short) 0); 
//	      cell5.setCellValue("");  
//	      cell5.setCellStyle(style);  
//	      cell5 = row5.createCell((short) 1); 
//	      cell5.setCellValue("");  
//	      cell5.setCellStyle(style); 
//	      titleCellNum = 2;
//		for (int s = 0, size = equipList.size(); s < size; s++) {
//			cell5 = row5.createCell((short) titleCellNum++);
//			cell5.setCellValue("温度(℃)");
//			cell5.setCellStyle(style);
//			cell5 = row5.createCell((short) titleCellNum++);
//			cell5.setCellValue("湿度(%RH)");
//			cell5.setCellStyle(style);
//		}
//	     
//	      
//	      
//	      int rowIndex = 5;
//	      int startCell = 0;
//	      BeanForRecord beanForRecord = null;
//		if (beanList != null) {
//			for (int i = 0, size = beanList.size(); i < size; i++) {
//				EquipMoreHistoryDataBean bean = beanList.get(i);
//				HSSFRow tmpRow = sheet.createRow((int) rowIndex++);
//
//				HSSFCell tmpCell = tmpRow.createCell((short) 0);
//				tmpCell.setCellValue(String.valueOf(i + 1));
//				tmpCell.setCellStyle(style);
//
//				tmpCell = tmpRow.createCell((short) 1);
//				tmpCell.setCellValue(bean.getRecordDate());
//				tmpCell.setCellStyle(style);
//
//				startCell = 2;
//				if (bean.getDatas() != null) {
//					for (int k = 0, kSize = equipList.size(); k < kSize; k++) {
//						beanForRecord = bean.getRecord(k);
//						if (beanForRecord == null) {
//							tmpCell = tmpRow.createCell((short) startCell++);
//							tmpCell.setCellValue("-");
//							tmpCell.setCellStyle(style);
//							tmpCell = tmpRow.createCell((short) startCell++);
//							tmpCell.setCellValue("-");
//							tmpCell.setCellStyle(style);
//							continue;
//						}
//						tmpCell = tmpRow.createCell((short) startCell++);
//						tmpCell.setCellValue(beanForRecord.getTemperature());
//						tmpCell.setCellStyle(style);
//						tmpCell = tmpRow.createCell((short) startCell++);
//						tmpCell.setCellValue(beanForRecord.getHumidity());
//						tmpCell.setCellStyle(style);
//					}
//				}
//			}
//		}
//	      
//		try {
//			ByteArrayOutputStream out = new ByteArrayOutputStream();
//			wb.write(out);
//			return new ByteArrayInputStream(out.toByteArray());
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//			
//		
////		 try {
////				wb.write(new FileOutputStream(file));
////			} catch (Exception e) {
////				e.printStackTrace();
////			}  
//				
//	}
//
//}
//
