package com.hs3.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class ContextListener
        implements ServletContextListener {
    private static LinkedBlockingQueue<Map<String, Object>> queue = new LinkedBlockingQueue();

    public void contextDestroyed(ServletContextEvent arg0) {
    }

    public void contextInitialized(ServletContextEvent arg0) {
        synchronized (queue) {
            if (queue.isEmpty()) {
                for (int i = 0; i < 3000; i++) {
                    try {
                        Map<String, Object> map = new HashMap(4);
                        queue.put(map);
                    } catch (InterruptedException localInterruptedException) {
                    }
                }
            }
        }
    }

    public static LinkedBlockingQueue<Map<String, Object>> getQueue() {
        return queue;
    }

    public static void setQueue(Map<String, Object> map) {
        try {
            queue.put(map);
        } catch (InterruptedException localInterruptedException) {
        }
    }
}
