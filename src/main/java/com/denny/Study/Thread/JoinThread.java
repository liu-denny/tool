package com.denny.Study.Thread;


public class JoinThread {

    public static Thread MyThread(String role,String api1){
        return new Thread(() -> {
            System.out.println("程序员" + role + api1);
        });
    }

    public static void main(String[] args) throws InterruptedException {
        Thread role1 = MyThread("小明","完成开发接口：/login");
        Thread role2 = MyThread("小王","完成开发接口：/logout");

        role1.start();
        role2.start();

//        role1.join();
//        role2.join();

        Thread role3 = MyThread("前端","完成对接");
        role3.start();
//        role3.join();
        System.out.println("测试开始测试");
    }
}
