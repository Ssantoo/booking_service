package com.example.booking.domain.queue;

public class RedisQueue {

    private static final String QUEUE_KEY = "WAIT";

    public static String getQueueKey() {
        return QUEUE_KEY;
    }


}
