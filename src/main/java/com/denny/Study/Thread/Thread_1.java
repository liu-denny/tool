package com.denny.Study.Thread;

public class Thread_1 implements Runnable{

    @Override
    public void run() {
        for (int x = 0; x < 500; x++) {
            System.out.println(x);
        }
    }
}
