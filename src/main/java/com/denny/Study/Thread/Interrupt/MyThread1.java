package com.denny.Study.Thread.Interrupt;

public class MyThread1 extends Thread {

    public MyThread1(String name){
        super(name);
    }

    @Override
    public void run(){
        try{
            int i =0;
            while (!isInterrupted()){
                Thread.sleep(50);
                System.out.println(Thread.currentThread().getName() +"状态"+this.getState() + "number" + i);
                i++;
            }
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() +"状态"+this.getState()+"，catch InterruptedException.");
        }
    }
}
