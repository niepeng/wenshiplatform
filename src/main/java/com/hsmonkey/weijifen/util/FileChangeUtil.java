package com.hsmonkey.weijifen.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wint.lang.utils.IoUtil;

/**
 * <p>标题: </p>
 * <p>描述: </p>
 * <p>版权: lsb</p>
 * <p>创建时间: 2016年9月24日  下午5:11:47</p>
 * <p>作者：niepeng</p>
 */
public class FileChangeUtil {
	
	public static int BUF_SIZE = 1024 * 8;
	protected final static Logger log = LoggerFactory.getLogger(FileChangeUtil.class);
	private static final String ffmpegMac = "/Users/lsb/data/code/yubao/weilu/weijifen/libs/ffmpegMac";
	private static final String ffmpegLinux = "/data/admin/java_tools/ffmpeg/ffmpeglinux";

	public static byte[] getFileByte(File file) {
		FileInputStream in = null;
		ByteArrayOutputStream out = null;
		try {
			in = new FileInputStream(file);
			out = new ByteArrayOutputStream(BUF_SIZE);
			byte[] b = new byte[BUF_SIZE];
			int n;
			while ((n = in.read(b)) != -1) {
				out.write(b, 0, n);
			}
			return out.toByteArray();
		} catch(Exception e) {
			
		}
		finally {
			IoUtil.close(in);
			IoUtil.close(out);
		}
		return null;
	}
	
	public static boolean hasFileData(File file) {
		byte[] data = getFileByte(file);
		return data != null && data.length > 0;
	}
	
	public static void deleteFile(File file) {
		if (file != null && file.exists()) {
			file.delete();
		}
	}
	
	 /** 
     * 将一个amr文件转换成mp3文件 
     * @param amrFile  
     * @param mp3File  
     * @throws IOException  
     */  
    private static void amr2mp3(String ffmpegPath, String amrFileName, String mp3FileName) throws IOException {  
    		Runtime runtime = Runtime.getRuntime();  
//        Process process = runtime.exec(ffmpegPath + " -i "+amrFileName+" -ar 8000 -ac 1 -y -ab 12.4k " + mp3FileName);  
        Process process = runtime.exec(ffmpegPath + " -i "+ amrFileName + " " + mp3FileName);  
        InputStream in = process.getErrorStream();  
        BufferedReader br = new BufferedReader(new InputStreamReader(in));  
        try {  
            String line = null;  
            while((line = br.readLine())!=null) {  
                System.out.println(line);  
            }  
            int result = process.waitFor();
            System.out.println(result);
            if(process.exitValue() != 0) {  
                //如果转换失败，这里需要删除这个文件（因为有时转换失败后的文件大小为0）  
//                new File(mp3FileName).delete();  
                throw new RuntimeException("转换失败！");  
            }  
        } catch (Exception e) {
        		log.error("amr2mp3Error", e);
		} finally {  
            //为了避免这里抛出的异常会覆盖上面抛出的异常，这里需要用捕获异常。  
            try {  
                in.close();  
            } catch (Exception e) {  
            }  
        }  
    }  
}  

