package com.redis.lua;

import java.util.List;

public class ThreadSeckillServiceTest extends Thread {
    private SeckillService service;
	private List<String> keys;
	private List<String> count;

    public ThreadSeckillServiceTest(SeckillService service, List<String> keys, List<String> count) {
		super();
		this.service = service;
		this.keys = keys;
		this.count = count;
	}



	@Override
    public void run() {
        service.seckill(keys, count);
    }
}
