package com.denny.DataStory.Jira.Jira148;

import com.denny.DataStory.Jira.Jira129.Util;

import java.util.*;

/**
 * @Description
 * @auther denny
 * @create 2020-02-15 16:13
 */
public class Jira {


    //计算最近的四个点
    public static List<D> getNear(List<D> dList,double lat,double lng){
        List<Double> distanceList = new ArrayList<>();
        Map<Double,D> map = new HashMap<>();
        for (D d : dList) {
            double distance = Util.GetDistance(d.getLat(),d.getLng(),lat,lng);
            distanceList.add(distance);
            map.put(distance,d);
        }
        Collections.sort(distanceList);
        Collections.reverse(distanceList);
        List<D> result = new ArrayList<>();
        for(int i=0;i<4;i++){
            result.add(map.get(distanceList.get(i)));
        }
        return result;
    }

    public static final List<String> residentList = Arrays.asList(
            "APP偏好大类-健康",
            "APP偏好大类-出行",
            "APP偏好大类-娱乐",
            "APP偏好大类-摄影",
            "APP偏好大类-教育",
            "APP偏好大类-旅游",
            "APP偏好大类-理财",
            "APP偏好大类-生活",
            "APP偏好大类-社交",
            "APP偏好大类-购物",
            "人生阶段-上班族",
            "人生阶段-中学生",
            "人生阶段-大学生",
            "人生阶段-自由职业",
            "人生阶段-退休",
            "到访偏好大类-医疗保健",
            "到访偏好大类-娱乐休闲",
            "到访偏好大类-教育学校",
            "到访偏好大类-汽车",
            "到访偏好大类-酒店宾馆",
            "婚姻状态-已婚",
            "婚姻状态-未婚",
            "子女年龄-13-18",
            "子女年龄-6-12岁",
            "子女年龄-<6岁",
            "子女年龄-孕婴",
            "居住社区价格等级-中",
            "居住社区价格等级-低   ",
            "居住社区价格等级-次低",
            "居住社区价格等级-次高",
            "居住社区价格等级-高",
            "年龄-0-17",
            "年龄-18-24",
            "年龄-25-30",
            "年龄-31-35",
            "年龄-36-40",
            "年龄-41-45",
            "年龄-46-60",
            "年龄-61以上",
            "性别-女",
            "性别-男",
            "手机价格-0-1000",
            "手机价格-1001-2000",
            "手机价格-2001-3000",
            "手机价格-3001-4000",
            "手机价格-4000-4001",
            "手机价格-5001-6000",
            "手机价格-6001-7000",
            "手机价格-7001-8000",
            "手机价格-8001-9000",
            "手机价格-9001-10000",
            "手机品牌-苹果",
            "旅游距离-中途",
            "旅游距离-无出游",
            "旅游距离-近途",
            "旅游距离-长途",
            "是否有车-否",
            "是否有车-是",
            "消费水平-中",
            "消费水平-低",
            "消费水平-次低",
            "消费水平-次高",
            "消费水平-高",
            "通勤方式-公交",
            "通勤方式-自驾",
            "餐馆消费价格-101-150元",
            "餐馆消费价格-151-200元",
            "餐馆消费价格-201-300元",
            "餐馆消费价格-301-500元",
            "餐馆消费价格-500元以上",
            "餐馆消费价格-50元以内",
            "餐馆消费价格-51-100元"
    );

    public static final List<String> keywords = Arrays.asList(
            "家具家居建材",
            "母婴儿童",
            "家政",
            "月嫂保姆",
            "亲子",
            "汽车",
            "妇产科",
            "儿科",
            "小学",
            "幼儿园",
            "幼儿教育"
            );

}
