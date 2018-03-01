package com.redis.lua;

public class ThreadSeckillService extends Thread {
    private SeckillService service;
    private int count;
    private String key;
    
    public ThreadSeckillService(SeckillService service,int count,String key) {
        this.service = service;
        this.count=count;
        this.key=key;
    }

    @Override
    public void run() {
        service.seckill(key,count);
    }
}
