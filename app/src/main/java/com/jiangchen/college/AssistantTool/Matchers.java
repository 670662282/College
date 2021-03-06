package com.jiangchen.college.AssistantTool;

/**
 * Created by Dell- on 2015/12/5 0005.
 * 正则匹配规则
 */
public class Matchers {

    public static String PHONE_MATCH = "^1(3|4|5|7|8)\\d{9}$";
    public static String EMAIL_MATCH = "^\\w+@\\w+\\.(com|cn)$";
    public static String PWD_MATHCH = "^\\w{6,20}$";
    public static String JSON_MATHCH = "^\\{.*\\}$";
    public static String SCHOOL_MATHCH = ".*(学校|学院|校区|大学)(校区)?";
    public static String PHONE_BE_REPLOCE = "(\\d{3})\\d{6}(\\d{2})";
    public static String PHONE_REPLOCEMENT = "$1******$2";



    public static String replace(String phone){
        return phone.replaceAll(Matchers.PHONE_BE_REPLOCE, Matchers.PHONE_REPLOCEMENT);
    }

}
