package com.hs3.service.quartz;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service("CrawlersTrigger")
public class CrawlersTrigger
        implements Serializable {
    private static final long serialVersionUID = -3653359868647364127L;

    class MyThread
            extends Thread {
        private final Logger logger = LoggerFactory.getLogger(MyThread.class);
        private String methodName;
        private String[] arg;
        private String[] argnumber;
        boolean isover = false;
        int Count = 0;

        public MyThread(String[] args, String[] argNum, String Name) {
            this.methodName = Name;
            this.arg = args;
            this.argnumber = argNum;
        }

        public void run() {
            try {
                if (this.methodName.equals("0")) {
                    try {
                        Thread.sleep(3000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    this.argnumber[0] = "shuzhu0";
                }
                if (this.methodName.equals("1")) {
                    this.argnumber[1] = "shuzhu1";
                }
                if (this.methodName.equals("2")) {
                    this.argnumber[2] = "shuzhu2";
                }
                if (this.methodName.equals("zongxiancheng")) {
                    while (!this.isover) {
                        System.out.println(this.argnumber[0] + "--" + this.argnumber[1] +
                                "--" + this.argnumber[2]);
                        try {
                            Thread.sleep(50L);
                            this.Count += 1;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if ((!this.argnumber[0].equals("")) && (!this.argnumber[1].equals("")) && (!this.argnumber[2].equals(""))) {
                            this.isover = true;
                        }
                        if (this.Count == 100) {
                            this.isover = true;
                        }
                    }
                    if ((this.argnumber[0].equals(this.argnumber[1])) || (this.argnumber[0].equals(this.argnumber[2])) || (this.argnumber[1].equals(this.argnumber[2]))) {
                        System.out.println("获取号码成功");
                    } else {
                        System.out.println("获取号码失败");
                    }
                } else {
                    System.out.println(this.arg[0] + "--" + this.arg[1] + "--" + this.methodName +
                            this.argnumber[0] + this.argnumber[1]);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    public void triggerOpenSeason(String triggerName)
            throws InterruptedException {
        String[] argStrings = {"http:www.baidu.com", "<>*?"};
        String[] argNum = {"", "", ""};
        for (int i = 0; i < 3; i++) {
            argStrings[0] = ("http:www.baidu.com" + Integer.toString(i));
            argStrings[1] = Integer.toString(i);
            Thread t = new MyThread(argStrings, argNum, Integer.toString(i));
            t.start();
            Thread.sleep(50L);
        }
        try {
            Thread.sleep(1000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Thread t = new MyThread(argStrings, argNum, "zongxiancheng");
        t.start();
    }

    public void triggerCreateSeason() {
        System.out.println("triggerMethodEx");
    }
}
