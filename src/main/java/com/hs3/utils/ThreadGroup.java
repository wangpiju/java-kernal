package com.hs3.utils;

import java.util.concurrent.CountDownLatch;

public abstract class ThreadGroup
        implements Runnable {
    private CountDownLatch countDownLatch;

    private ThreadGroup(CountDownLatch count) {
        this.countDownLatch = count;
    }

    public void run() {
        work();
        this.countDownLatch.countDown();
    }

    public abstract void work();
}
