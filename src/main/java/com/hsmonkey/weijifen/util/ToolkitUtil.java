package com.hsmonkey.weijifen.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.beanutils.BeanUtils;




/**
 *
 *<p>标题:EasyFrame.com.util.Toolkit</p>
 *<p>描述:反编译Toolkit.getmap方法</p>
 *<p>版权:驭宝</p>
 *<p>创建时间: 2015年9月15日  上午10:42:01</p>
 *<p>作者: yingmu</p>
 */

public class ToolkitUtil {
	  public static Map getMap(Collection colletion, String keyName) {
		    if (colletion == null) {
		      return null;
		    }
		    Object curObj = null;
		    String curKey = null;
		    Map map = new HashMap();
		    try {
		      for (Iterator it = colletion.iterator(); it.hasNext(); ) {
		        curObj = it.next();
		        curKey = BeanUtils.getProperty(curObj, keyName);
		        if (!map.containsKey(curKey))
		          map.put(curKey, curObj);
		        else
		          map.put(curKey + "_" + curObj.hashCode(), curObj);
		      }
		    }
		    catch (Exception e) {
		      throw new IllegalStateException(e.getClass() + "--" + e.getMessage());
		    }

		    return map;
		  }
	  
	  public static String getString(Object obj)
	  {
	    if (obj != null) {
	      return String.valueOf(obj);
	    }
	    return "";
	  }

	  public static List getStrList(String str, String sep)
	  {
	    if ((str != null) && (!str.equals("")) && (sep != null))
	    {
	      int iSep = sep.length();
	      Vector vct = new Vector();
	      for (int pos = str.indexOf(sep); pos > -1; ) {
	        vct.add(str.substring(0, pos));
	        str = str.substring(pos + iSep);
	        pos = str.indexOf(sep);
	      }
	      vct.addElement(str);
	      return vct;
	    }
	    return null;
	  }

}
