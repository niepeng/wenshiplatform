package com.hsmonkey.weijifen;
/**
 * Created by lsb on 17/11/16.
 */


import com.hsmonkey.weijifen.biz.bean.MobileDeviceBean;
import com.hsmonkey.weijifen.util.JsonUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author 聂鹏
 * @version 1.0
 * @email lisenbiao@51huadian.cn
 * @date 17/11/16
 */

public class Main {

  public static void main(String[] args) throws JSONException {
//		String psw = "123456";
//		System.out.println(MD5.encrypt(psw));

    MobileDeviceBean bean = new MobileDeviceBean();
    bean.setUser("cqy");
    List<String> cannelSmsPhoneList = new ArrayList<>();
    cannelSmsPhoneList.add("13012345678");
    bean.setDeviceMobileList(cannelSmsPhoneList);
    Map<String, String> headerMap = new HashMap<String, String>();
    headerMap.put("TYPE", "delMobileToDevice");
    String body = JsonUtil.fields("user,deviceMobileList", bean);
    System.out.println(body);

  }
}