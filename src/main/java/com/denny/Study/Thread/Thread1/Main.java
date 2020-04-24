package com.denny.Study.Thread.Thread1;

/**
 * @Description
 * @auther denny
 * @create 2020-04-22 20:53
 */
public class Main {

    public static void main(String[] args) {
        Target target = new Target(0,500);

        Thread thread1 = new Thread(target);
        Thread thread2 = new Thread(target);

        thread1.start();
        thread2.start();
    }
}
