package com.denny.Study.Thread.Thread1;

/**
 * @Description
 * @auther denny
 * @create 2020-04-22 20:46
 */
public class Target implements Runnable{
    private int start;
    private int end;
    private int count;

    public Target(int start, int end) {
        this.start = start;
        this.end = end;
        this.count = 0;
    }

    public void sum(){
        while (true){
            if(start<end){
                count++;
                start++;
            }else {
                System.out.println(count);
                break;
            }
        }

    }

    @Override
    public void run() {
        sum();
    }
}
