package com.denny.Test;

import com.denny.Utils.ReadFileUtils;

import java.util.List;

/**
 * @Description
 * @auther denny
 * @create 2020-02-27 18:37
 */
public class T {
    public static void main(String[] args) {
        String path = "D:\\work\\tool\\src\\main\\resources\\dataSort.txt";
        List<String> list =  ReadFileUtils.readTxtFileIntoStringArrList(path);
//        for (String s : list) {
//            System.out.println(s);
//        }
        list.forEach(System.out::println);
    }
    }
