package com.hsmonkey.weijifen.web.common.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import wint.mvc.flow.FlowData;
import wint.mvc.flow.ServletFlowData;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2016年3月31日  下午3:05:47</p>
 * <p>作者：niepeng</p>
 */
public class CSVManager {

	public static StringBuilder quotes = new StringBuilder("\"");
	public static StringBuilder split  = new StringBuilder(",");
	private final static byte commonCsvHead[] = { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };

	public static File createCSVFile(File csvFile, String[] titles, List<String> dataList) {
		OutputStream os = null;
//		String lineSplit = java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"));
		String lineSplit = "\n";

		try {
			os = new FileOutputStream(csvFile);
			os.write(commonCsvHead);
			os.write(genLine(titles).getBytes("UTF-8"));
			os.write(lineSplit.getBytes());
			for (int i = 0, size = dataList.size(); i < size; i++) {
				os.write(dataList.get(i).getBytes("UTF-8"));
				os.write(lineSplit.getBytes());
			}
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return csvFile;
	}

	public static String genLine(String[] array) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			if (i > 0) {
				sb.append(split);
			}
			sb.append(quotes);
			sb.append(array[i]);
			sb.append(quotes);
		}
		return sb.toString();
	}

	public static StringBuilder appendLine(String... strings) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < strings.length; i++) {
			if (i > 0) {
				sb.append(split);
			}
			sb.append(quotes);
			sb.append(strings[i]);
			sb.append(quotes);
		}
		return sb;
	}
	
	public static BufferedReader readCsv(String fileFullPath, String charset) {
		try {
			return new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileFullPath)), charset));
		} catch (Exception e) {
		}
		return null;
	}
	
	public static void close(BufferedReader br) {
		try {
			if(br != null) {
				br.close();
			}
		} catch (Exception e) {
		}
	}
	
	public static String readLine(BufferedReader br) throws IOException {
		StringBuffer readLine = new StringBuffer();
		boolean bReadNext = true;

		while (bReadNext) {
			//
			if (readLine.length() > 0) {
				readLine.append("\r\n");
			}
			// 一行
			String strReadLine = br.readLine();

			// readLine is Null
			if (strReadLine == null) {
				return null;
			}
			readLine.append(strReadLine);

			// 如果双引号是奇数的时候继续读取。考虑有换行的是情况。
			if (countChar(readLine.toString(), '"', 0) % 2 == 1) {
				bReadNext = true;
			} else {
				bReadNext = false;
			}
		}
		return readLine.toString();
	}
	
	 /** 
     * 把CSV文件的一行转换成字符串数组。不指定数组长度。 
     */  
	public static List<String> fromCSVLinetoArray(String source) {
		if (source == null || source.length() == 0) {
			return null;
		}
		int currentPosition = 0;
		int maxPosition = source.length();
		int nextComma = 0;
		List<String> rtnArray = new ArrayList<String>();
		while (currentPosition < maxPosition) {
			nextComma = nextComma(source, currentPosition);
			rtnArray.add(nextToken(source, currentPosition, nextComma));
			currentPosition = nextComma + 1;
			if (currentPosition == maxPosition) {
				rtnArray.add("");
			}
		}
		return rtnArray;
	}
	
	
	/** 
     *计算指定文字的个数。 
     * 
     * @param str 文字列 
     * @param c 文字 
     * @param start  开始位置 
     * @return 个数 
     */  
    private static int countChar(String str, char c, int start) {  
        int i = 0;  
        int index = str.indexOf(c, start);  
        return index == -1 ? i : countChar(str, c, index + 1) + 1;  
    } 
    
    
    /** 
     * 查询下一个逗号的位置。 
     * 
     * @param source 文字列 
     * @param st  检索开始位置 
     * @return 下一个逗号的位置。 
     */  
    private static int nextComma(String source, int st) {  
        int maxPosition = source.length();  
        boolean inquote = false;  
        while (st < maxPosition) {  
            char ch = source.charAt(st);  
            if (!inquote && ch == ',') {  
                break;  
            } else if ('"' == ch) {  
                inquote = !inquote;  
            }  
            st++;  
        }  
        return st;  
    }  
  
    /** 
     * 取得下一个字符串 
     */  
    private static String nextToken(String source, int st, int nextComma) {  
        StringBuffer strb = new StringBuffer();  
        int next = st;  
        while (next < nextComma) {  
            char ch = source.charAt(next++);  
            if (ch == '"') {  
                if ((st + 1 < next && next < nextComma) && (source.charAt(next) == '"')) {  
                    strb.append(ch);  
                    next++;  
                }  
            } else {  
                strb.append(ch);  
            }  
        }  
        return strb.toString();  
    }

	public static String genCsvOutputStream(List<String> titleList, List<List<String>> contentList) {
		 StringBuffer strb = new StringBuffer(); 
		 for (int i = 0; i < titleList.size(); i++) {
			 if(i>0){
				 strb.append(",");
			 }
			 strb.append(titleList.get(i));
		}
		for (int i = 0; i < contentList.size(); i++) {
			strb.append("\n");
			for (int j = 0; j < contentList.get(i).size(); j++) {
				 if(j> 0){
					 strb.append(",");
				 }
				 strb.append(contentList.get(i).get(j));
			} 
		} 
		return strb.toString();
	}
	
	public static void outWriteTxt(List<String> dataList, FlowData flowData, String fileName) {
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
			response.setContentType("text/plain");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			PrintWriter out = response.getWriter();
			for (int i = 0, size = dataList.size(); i < size; i++) {
				out.print(dataList.get(i));
				out.print("\r\n");
			}
			out.flush();
		} catch (Exception e) {
		} finally {
			try {
				// out.close();
			} catch (Exception e) {
			}
		}
	}

	public static void outWrite(String strOutputStream, FlowData flowData, String fileName, String charset) {
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
			
//			ServletOutputStream out = response.getOutputStream();
//			//加上utf-8的BOM
			
			response.reset();
			response.setContentType("application/csv;charset=" + charset);
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
//		    response.setContentType("application/csv;charset=gb18030"); 
//			response.setHeader("Content-Disposition","attachment;  filename=x.csv"); 
			PrintWriter out = response.getWriter();
			out.print(new String(new byte[]{(byte)0xEF,(byte)0xBB,(byte)0xBF},"UTF-8"));
			out.println(new String(strOutputStream.getBytes("UTF-8"),"UTF-8"));
//			out.write(new byte[]{(byte)0xEF,(byte)0xBB,(byte)0xBF});
//			out.println(strOutputStream);
			out.flush();
		} catch (Exception e) {
		} finally {
			try {
//				out.close();
			} catch (Exception e) {
			}
		}
	}  
}
