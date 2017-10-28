package com.hsmonkey.weijifen;

import com.hsmonkey.weijifen.biz.bean.MobileDeviceBean;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.hsmonkey.weijifen.biz.bean.TestAlarmBean;
import com.hsmonkey.weijifen.biz.bean.TestEquBean;
import com.hsmonkey.weijifen.util.JsonUtil;
import java.util.Map;

public class TestJsonUtil {

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("user", "a");
        map.put("beepStatus", "0");
        String content = JsonUtil.mapToJson(map);
        System.out.println(content);
    }

    public static void main3(String[] args) {
//        Map<String, String> map = new HashMap<>();
//        map.put("user", "xsf");
//
//        String content = JsonUtil.mapToJson(map);
//        System.out.println(content);
        MobileDeviceBean bean = new MobileDeviceBean();
        bean.setUser("sf");
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        bean.setDeviceMobileList(list);
        bean.setSnaddr(list);
        String content = JsonUtil.fields("user,snaddr,deviceMobileList", bean);
        System.out.println(content);
    }
    
    public static void main2(String[] args) {
        TestAlarmBean test = new TestAlarmBean();
        test.setAddress(1);
        test.setAlarmTime(new Date());
        test.setLastAlarmTime(null);
        test.setReason("hellow");
        String value = JsonUtil.fields("address,reason,alarmTime,lastAlarmTime", test);
        System.out.println(value);
    }

    public static void main1(String[] args) {
        TestEquBean equBean = new TestEquBean();
        equBean.setAddress(1);
        equBean.setEqutype(2);
        equBean.setMark("woshimark");
        equBean.setTempValue(13);
        equBean.setHumiValue(14);
        equBean.setReferId(55);
        List<TestEquBean> list = new ArrayList<TestEquBean>();
        list.add(equBean);
        list.add(equBean);
        String valueString = JsonUtil.mfields("address,equtype,mark,tempValue,humiValue,referId", list);
        System.out.println(valueString);
    }
}
