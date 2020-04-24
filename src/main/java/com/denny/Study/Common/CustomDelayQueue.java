package com.denny.Study.Common;

import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @auther denny
 * @create 2020-04-16 19:05
 */
public class CustomDelayQueue{
    private static DelayQueue delayQueue = new DelayQueue();

    static class MyDelay implements Delayed {
        long delayTime = System.currentTimeMillis();
        private String msg;

        public MyDelay(long delayTime,String msg){
            this.delayTime = (this.delayTime + delayTime);
            this.msg = msg;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(delayTime - System.currentTimeMillis(),TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            if(this.getDelay(TimeUnit.MILLISECONDS) > o.getDelay(TimeUnit.MILLISECONDS)){
                return 1;
            }else if(this.getDelay(TimeUnit.MILLISECONDS) < o.getDelay(TimeUnit.MILLISECONDS)){
                return 0;
            }else {
                return 0;
            }
        }

        @Override
        public String toString(){
            return this.msg;
        }
    }

    public static void producer(){
        delayQueue.put(new MyDelay(1000,"消息1"));
        delayQueue.put(new MyDelay(3000,"消息2"));
    }

    public static void consumer() throws InterruptedException {
        System.out.println("开始时间：" + DateFormat.getDateTimeInstance().format(new Date()));
        while (!delayQueue.isEmpty()){
            System.out.println(delayQueue.take());
        }
        System.out.println("结束时间：" + DateFormat.getDateTimeInstance().format(new Date()));
    }

    public static void main(String[] args) throws InterruptedException {
        producer();
        consumer();
    }

}
