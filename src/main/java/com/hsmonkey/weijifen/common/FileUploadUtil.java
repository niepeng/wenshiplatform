package com.hsmonkey.weijifen.common;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;

import wint.lang.utils.StringUtil;

import com.hsmonkey.weijifen.util.DateUtil;

/**
 * <p>标题: 针对图片上传的工具类，包含压缩等</p>
 * <p>描述: </p>
 * <p>创建时间: 2015-12-8  下午3:50:32</p>
 * <p>作者：niepeng</p>
 */
public class FileUploadUtil {

	private static final String BUCKET_NAME = "weijifenfiles";
	private static final String OPERATOR_NAME = "weijifen";
	private static final String OPERATOR_PWD = "weijifen4321";

	/** 目录名 */
	private static final String FULL_FOLDER_NAME = "/uploadimg/";

	/** 访问地址的前缀 */
	private static final String VISIT_HTTP_PREFIX = "http://weijifenfiles.b0.upaiyun.com/uploadimg/";

	/**
	 * 根据图片绝对文件系统地址，生成小容量的文件
	 *
	 * @param fileFullPath
	 * @return  返回文件绝对路径地址
	 */
	public static String genCompress(String fileFullPath, String defaultName) {
		try {
			// read image file
			BufferedImage bufferedImage = ImageIO.read(new File(fileFullPath));
			// create a blank, RGB, same width and height, and a white
			// background
			BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
			newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
			// write to jpeg file
			String descFileFullPath = fileFullPath.substring(0, fileFullPath.lastIndexOf("/")  + 1) + (StringUtil.isBlank(defaultName) ? (System.currentTimeMillis() + ".jpg") : defaultName) ;
			ImageIO.write(newBufferedImage, "jpg", new File(descFileFullPath));
			return descFileFullPath;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据图片绝对路径 和 建议上传后的图片名称  上传图片到又拍云，返回http可以访问的地址
	 *
	 * @param fileFullPath
	 * @param adviceFileName
	 * @return
	 */
	public static String uploadImg2upaiyun(String fileFullPath, String adviceFileName) {
		try {
			if (fileFullPath == null) {
				return null;
			}
			UpYun upyun = new UpYun(BUCKET_NAME, OPERATOR_NAME, OPERATOR_PWD);
			String tmpFileName = StringUtil.isBlank(adviceFileName) ? (System.currentTimeMillis() + ".jpg") : adviceFileName;
			String datePath = DateUtil.format(new Date(), DateUtil.DATE_YD) + "/";
			String filePath = FULL_FOLDER_NAME + datePath + tmpFileName;
			// 本地待上传的图片文件
			File file = new File(fileFullPath);
			upyun.setTimeout(120);
			// 设置待上传文件的 Content-MD5 值
			// 如果又拍云服务端收到的文件MD5值与用户设置的不一致，将回报 406 NotAcceptable 错误
			upyun.setContentMD5(UpYun.md5(file));

			// 设置待上传文件的"访问密钥"
			// 注意：设置密钥后，无法根据原文件URL直接访问，需带URL后面加上（缩略图间隔标志符+密钥）进行访问
			// 举例：
			// 如果缩略图间隔标志符为"!"，密钥为"bac"，上传文件路径为"/folder/test.jpg"，
			// 那么该图片的对外访问地址为：http://空间域名 /folder/test.jpg!bac
			// upyun.setFileSecret("bac");

			// 上传文件，并自动创建父级目录（最多10级）
			boolean result = upyun.writeFile(filePath, file, true);
			if (result) {
				return VISIT_HTTP_PREFIX + datePath + tmpFileName;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void main(String[] args) throws IOException {
//		File file = new File("/Users/lsb/data/code/yubao/trunk/item-master/src/test/java/com/yubaoseo/itemmaster/biz/testDetail.txt");
//		String content = FileUtil.readAsString(file, "utf-8");
//		String result = html2File(content, "/Users/lsb/data/code/yubao/trunk/item-master/src/test/java/com/yubaoseo/itemmaster/biz/");
//		System.out.println(result);
//		String smallFilePath = genCompress(result,   result.substring(result.lastIndexOf("/") + 1, result.indexOf(".")) +  "_1.jpg");
//		System.out.println(smallFilePath);

//		String fileFullPath = "/Users/lsb/data/code/yubao/trunk/item-master/src/test/java/com/yubaoseo/itemmaster/biz/1449562185017_1.jpg";
		String fileFullPath = "/Users/lsb/Desktop/atest1.jpg";
		String httpImg = uploadImg2upaiyun(fileFullPath, null);
		System.out.println(httpImg);
	}


}
