package com.denny.Study.Thread.Interrupt;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        try{
            Thread t1 = new MyThread1("t1");
            System.out.println(t1.getName() + "状态" + t1.getState());

            t1.start();
            System.out.println(t1.getName() + "状态" + t1.getState());

            Thread.sleep(300);
            //向线程t1发出中断指令
            t1.interrupt();
            System.out.println(t1.getName() + "状态" + t1.getState() + "已经interrupt");

            Thread.sleep(100);
            System.out.println(t1.getName() + "状态" + t1.getState() + "已经interrupt");
        }catch (InterruptedException e){

        }

    }
}
