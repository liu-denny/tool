package com.denny.DataStory.PDF;

import sun.misc.BASE64Decoder;

import java.net.URLDecoder;

/**
 * @Description
 * @auther denny
 * @create 2020-03-25 14:00
 */
public class Base64 {
    public static void main(String[] args) throws Exception {
        String base64="JUU1JTkwJThEJUU1JUFEJTk3JUVGJUJDJTlBJUU2JTk1JUIwJUU4JUFGJUI0JUU2JTk1JTg1JUU0JUJBJThCJUU1JTkxJUE4JUU4JUJFJUI5JTBBJUU1JTlDJUIwJUU0JUJBJUE3JUU3JTk0JUI1JUU4JUFGJTlEJUVGJUJDJTlBJUU2JTlBJTgyJUU2JTk3JUEwJTBBJUU1JTlDJUIwJUU1JTlEJTgwJUVGJUJDJTlBJUU2JTlBJTgyJUU2JTk3JUEwJTBBJUU3JUE3JTlGJUU5JTg3JTkxJUVGJUJDJTlBJUU2JTlBJTgyJUU2JTk3JUEwJTBBJUU5JTlEJUEyJUU3JUE3JUFGJUVGJUJDJTlBJUU2JTlBJTgyJUU2JTk3JUEwJTBBJUU3JUIxJUJCJUU1JTlFJThCJUVGJUJDJTlBJUU1JTg2JTg1JUU4JUExJTk3JUU5JTkzJUJBJTBBJUU3JTgyJUI5JUU0JUJEJThEJUU4JUFGJTg0JUU0JUJDJUIwJUU1JUJFJTk3JUU1JTg4JTg2JUVGJUJDJTlBMjclMEE=";
        base64 = base64.replaceAll(" ", "+");
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] buffer;
        buffer = decoder.decodeBuffer(base64);
        String result;
        result = new String(buffer);

        String keyWord = URLDecoder.decode(result, "UTF-8");
//        try {
//            byte[] decode = Base64Util.decode(base64);
//            result = new String(decode);
//        }catch (Exception e){
//            throw new Exception(base64+"：格式错误！");
//        }
        System.out.println(keyWord);
    }


}
