/**
 *
 */
package com.hsmonkey.weijifen.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wint.lang.utils.StringUtil;

/**
 * @author niepeng
 *
 * @date 2012-9-12 上午1:50:26
 */
public class StringsUtil extends StringUtil {
	
	/**
	 * 从cotent的index位置开始，往前查找字符串targetStr的位置
	 * @param content
	 * @param index
	 * @param targetStr
	 * @return
	 */
	public static int findBeforeIndexOf(String content, int index, String targetStr) {
		StringBuilder sb = new StringBuilder(content);
		sb.reverse();
		content = sb.toString();
		int size = content.length();
		
		StringBuilder targetStrSb = new StringBuilder(targetStr);
		targetStrSb.reverse();
		int value = content.indexOf(targetStrSb.toString(), size - index -1);
		if(value >= 0) {
			return size - value - targetStr.length();
		}
		return -1;
	}
	
	/**
	 * 如果直接使用string的replaceAll方法，如果value中有 $ 等符号的时候，会出现如下错误：
	 * Exception in thread "main" java.lang.IllegalArgumentException: Illegal group reference
	 * 
	 * @param content 内容
	 * @param tag	替换的标签
	 * @param value  替换成的内容
	 * @return
	 */
	public static String replaceAll(String content, String tag, String value) {
		if (content == null || tag == null || value == null) {
			return content;
		}

		if (value.indexOf(tag) > 0) {
			return content;
		}

		int index = -1;
		while ((index = content.indexOf(tag)) >= 0) {
			content = content.substring(0, index) + value + content.substring(index + tag.length());
		}

		return content;
	}
	
	public static String base64Encode(String value) {
		return Base64Util.encode(value.getBytes());
	}
	
	public static String base64Decode(String value) {
		try {
			return new String(Base64Util.decode(value) , "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String urlDecode(String str) {
		try {
			return URLDecoder.decode(str, "utf-8");
		} catch (Exception e) {
		}
		return EMPTY;
	}

	public static String urlEecode(String str) {
		try {
			return URLEncoder.encode(str, "utf-8");
		} catch (Exception e) {
		}
		return EMPTY;
	}


    public static int str2int(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return 0;
    }

    public static long str2long(String str) {
        try {
            if (StringUtil.isBlank(str)) {
                return 0;
            }
            return Long.parseLong(str);
        } catch (Exception e) {
        }
        return 0;
    }

	public static double str2double(String str) {
		try {
			return Double.valueOf(str).doubleValue();
		} catch (Exception e) {
		}
		return 0.d;
	}
	
	public static String getValue(String content, String startStr, String lastStr) {
		if(content == null) {
			return null;
		}
		int indexStart = content.indexOf(startStr);
		int indexLast = content.lastIndexOf(lastStr);
		int size = content.length();
		if(size > indexLast && indexLast > indexStart) {
			return content.substring(indexStart + startStr.length(), indexLast);
		}
		return null;
	}
	
    public static String getTimeF() {
        String time = String.valueOf(System.currentTimeMillis());
        return time.substring(time.length() - 4, time.length());

    }

    public static final String EMPTY = "";

    public static final String DEFAULT_SPLIT = ",";

    /*
     * 把 isSuccess -> is_success
     */
    public static String reFormatArgs(String arg) {
        if (StringUtil.isBlank(arg)) {
            return EMPTY;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0, length = arg.length(); i < length; i++) {
            char c = arg.charAt(i);
            if (!Character.isLowerCase(c)) {
                sb.append("_");
                sb.append((char) (c + 32));

            } else {
                sb.append(c);
            }

        }
        return sb.toString();
    }

    public static String subIndex0(String str) {
        if (StringUtil.isBlank(str)) {
            return EMPTY;
        }
        String[] ss = str.split(DEFAULT_SPLIT);
        return ss[0];
    }

    public static String subUsername(String str) {
        if (StringUtil.isBlank(str)) {
            return EMPTY;
        }
        if (str.length() < 30) {
            return str;
        }
        return str.substring(0, 30);
    }

	public static <T> String list2Str(List<T> list, String split) {
		if (CollectionUtils.isEmpty(list)) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			if (i > 0) {
				sb.append(split);
			}
			sb.append(String.valueOf(list.get(i)));
		}
		return sb.toString();
	}

    public static String merge(String[] strArray, String split) {
        if (strArray == null || strArray.length == 0) {
            return EMPTY;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < strArray.length; i++) {
            result.append(strArray[i]);
            if (i + 1 != strArray.length) {
                result.append(split);
            }
        }
        return result.toString();
    }

    public static String trimWidthHeight(String content) {
        if (content == null || content.length() < 1) {
            return "";
        }
        // int startIndex = -1, endIndex = -1;
        //
        // while((startIndex = content.indexOf(UxiangConstant.HEIGHT_STR))> 0 &&
        // (endIndex = content.indexOf("\"", startIndex +
        // UxiangConstant.HEIGHT_STR.length() + 1)) > 0) {
        // String result1 = content.substring(0, startIndex);
        // String result2 = content.substring(endIndex+1);
        // content = result1 + result2;
        // }
        //
        // while((startIndex = content.indexOf(UxiangConstant.WIDTH_STR))> 0 &&
        // (endIndex = content.indexOf("\"", startIndex +
        // UxiangConstant.WIDTH_STR.length() + 1)) > 0) {
        // String result1 = content.substring(0, startIndex);
        // String result2 = content.substring(endIndex+1);
        // content = result1 + result2;
        // }

        // cellpadding cellspacing
        return content.replaceAll("style", "abc").replaceAll("width", "abc").replaceAll("height", "abc").replaceAll("cellpadding", "abc").replaceAll("cellspacing", "abc");

    }

    public static String merge(String[] strArray) {
        return merge(strArray, DEFAULT_SPLIT);
    }

    /**
     *
     * @param str
     *            需要过滤的字符串
     * @return
     * @Description:过滤数字以外的字符
     */
    public static String filterUnNumber(String str) {
        // 只允数字
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        // 替换与模式匹配的所有字符（即非数字的字符将被""替换）
        return m.replaceAll("").trim();

    }

    /**
     * 统计字符数 汉字算2个字符
     */
    public static int getNum(String str) {
        int count = 0;
        if (null == str || str.length() == 0) {
            return count;
        }
        String E1 = "[\u4e00-\u9fa5]";// 中文
        String temp;
        for (int i = 0; i < str.length(); i++) {
            temp = String.valueOf(str.charAt(i));
            if (temp.matches(E1)) {
                count += 2;
            } else {
                count++;
            }
        }
        return count;
    }

    /**
     * 过滤html代码
     *
     * @param inputString
     * @return
     */
    public static String Html2Text(String inputString) {
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        java.util.regex.Pattern p_script;
        java.util.regex.Matcher m_script;
        java.util.regex.Pattern p_style;
        java.util.regex.Matcher m_style;
        java.util.regex.Pattern p_html;
        java.util.regex.Matcher m_html;

        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
                                                                                                     // }
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
                                                                                                  // }
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签

            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签

            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签

            textStr = htmlStr;

        } catch (Exception e) {
            System.err.println("Html2Text: " + e.getMessage());
        }

        return textStr;// 返回文本字符串
    }

    // 以下为替换html标签
    private final static String regxpForHtml = "<([^>]*)>"; // 过滤所有以<开头以>结尾的标签

    // private final static String regxpForImgTag = "<*>"; // 找出IMG标签
    // private final static String regxpForImaTagSrcAttrib = "src=\"([^\"]+)\"";
    // // 找出IMG标签的SRC属性
    /**
     *
     * 基本功能：替换标记以正常显示
     * <p>
     *
     * @param input
     * @return String
     */
    public static String replaceTag(String input) {
        if (!hasSpecialChars(input)) {
            return input;
        }
        StringBuffer filtered = new StringBuffer(input.length());
        char c;
        for (int i = 0; i <= input.length() - 1; i++) {
            c = input.charAt(i);
            switch (c) {
            case '<':
                filtered.append("&lt;");
                break;
            case '>':
                filtered.append("&gt;");
                break;
            case '"':
                filtered.append("&quot;");
                break;
            case '&':
                filtered.append("&amp;");
                break;
            default:
                filtered.append(c);
            }
        }
        return (filtered.toString());
    }

    /**
     *
     * 基本功能：判断标记是否存在
     * <p>
     *
     * @param input
     * @return boolean
     */
    public static boolean hasSpecialChars(String input) {
        boolean flag = false;
        if ((input != null) && (input.length() > 0)) {
            char c;
            for (int i = 0; i <= input.length() - 1; i++) {
                c = input.charAt(i);
                switch (c) {
                case '>':
                    flag = true;
                    break;
                case '<':
                    flag = true;
                    break;
                case '"':
                    flag = true;
                    break;
                case '&':
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    /**
     *
     * 基本功能：过滤所有以"<"开头以">"结尾的标签
     * <p>
     *
     * @param str
     * @return String
     */
    public static String filterHtml(String str) {
        Pattern pattern = Pattern.compile(regxpForHtml);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result1 = matcher.find();
        while (result1) {
            matcher.appendReplacement(sb, "");
            result1 = matcher.find();
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     *
     * 基本功能：过滤指定标签
     * <p>
     *
     * @param str
     * @param tag
     *            指定标签
     * @return String
     */
    public static String fiterHtmlTag(String str, String tag) {
        String regxp = "<*" + tag + "*>";
        Pattern pattern = Pattern.compile(regxp);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result1 = matcher.find();
        while (result1) {
            matcher.appendReplacement(sb, "");
            result1 = matcher.find();
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     *
     * 基本功能：替换指定的标签
     * <p>
     *
     * @param str
     * @param beforeTag
     *            要替换的标签
     * @param tagAttrib
     *            要替换的标签属性值
     * @param startTag
     *            新标签开始标记
     * @param endTag
     *            新标签结束标记
     * @return String
     * @如：替换img标签的src属性值为[img]属性值[/img]
     */
    public static String replaceHtmlTag(String str, String beforeTag, String tagAttrib, String startTag, String endTag) {
        String regxpForTag = "<*" + beforeTag + "*>";
        String regxpForTagAttrib = tagAttrib + "=\"([^\"]+)\"";
        Pattern patternForTag = Pattern.compile(regxpForTag);
        Pattern patternForAttrib = Pattern.compile(regxpForTagAttrib);
        Matcher matcherForTag = patternForTag.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result = matcherForTag.find();
        while (result) {
            StringBuffer sbreplace = new StringBuffer();
            Matcher matcherForAttrib = patternForAttrib.matcher(matcherForTag.group(1));
            if (matcherForAttrib.find()) {
                matcherForAttrib.appendReplacement(sbreplace, startTag + matcherForAttrib.group(1) + endTag);
            }
            matcherForTag.appendReplacement(sb, sbreplace.toString());
            result = matcherForTag.find();
        }
        matcherForTag.appendTail(sb);
        return sb.toString();
    }

    /**
     * 将点号和后面的字母替换为大写
     * @Title: replacePointToUpper
     * @Description: 将点号和后面的字母替换为大写
     * @param srcStr
     * @return String
     * @throws
     */
    public static String replacePointToUpper(String srcStr){
        int index = srcStr.indexOf(".");
        String str = srcStr;
        if(index > 0){
            char firstChar = srcStr.charAt(index + 1);
            str = srcStr.replaceFirst("\\.([a-z]|[A-Z]){1}", String.valueOf(Character.toUpperCase(firstChar)));
        }

        return str;
    }
    /**
	 * @desc 截取指定字符之间的字符串
	 * @param v
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static String getBetween(String v, String v1, String v2) {
		String r = "";
		try {
			if (v.indexOf(v1) != -1 && v.indexOf(v2) != -1) {
				String tmp = v.substring(v.indexOf(v1) + v1.length()).toString();
				r = tmp.substring(0, tmp.indexOf(v2));
			}
		} catch (Exception e) {
		}
		return r.trim();
	}

	public static String str(String str) {
	  return str;
  }
}
