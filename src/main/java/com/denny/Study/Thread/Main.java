package com.denny.Study.Thread;

import java.io.File;

/**
 * @Description
 * @auther denny
 * @create 2020-04-22 20:35
 */
public class Main {
    public static void main(String[] args) {
        Thread_1 thread1 = new Thread_1();

        Thread t1 = new Thread(thread1);
        Thread t2 = new Thread(thread1);

        t1.start();
        t2.start();

    }
}
