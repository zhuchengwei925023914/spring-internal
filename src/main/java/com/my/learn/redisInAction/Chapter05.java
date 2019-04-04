package com.my.learn.redisInAction;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;

import java.security.Timestamp;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

/**
 * Created by zhu on 2017/5/26.
 */
public class Chapter05 {
    public static final String DEBUG = "debug";
    public static final String INFO = "info";
    public static final String WARNING = "warning";
    public static final String ERROR = "error";
    public static final String CRITICAL = "critical";

    public static final Collator COLLATOR = Collator.getInstance();

    public static final SimpleDateFormat TIMESTAMP =
            new SimpleDateFormat("EEE MMM dd HH:00:00 yyyy");
    public static final SimpleDateFormat ISO_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:00:00");

    static {
        ISO_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) throws InterruptedException {
        new Chapter05().run();
    }

    public void run() throws InterruptedException {
        Jedis conn = new Jedis("localhost");
        conn.select(15);

        testLogRecent(conn);
        testLogCommon(conn);
    }

    public void testLogRecent(Jedis conn) {
        System.out.println("\n------testLogRecent-----");
        System.out.println("Let's write a few logs to the recent log");
        for (int i = 0; i < 5; i++) {
            logRecent(conn, "test", "this is message " + i);
        }
        List<String> recent = conn.lrange("recent:test:info", 0, -1);
        System.out.println("The current recent message log has this many messages: " + recent.size());
        System.out.println("Those message include: ");
        for (String s : recent) {
            System.out.println(s);
        }
        assert recent.size() >= 5;
    }

    public void testLogCommon(Jedis conn) {
        System.out.println("\n-----testLogCommon-----");
        System.out.println("Let's write some items to the common log");
        for (int count = 1; count < 6; count++) {
            for (int i = 0; i < count; i++) {
                logCommon(conn, "test", "message-" + count);
            }
        }
        Set<Tuple> common = conn.zrevrangeWithScores("common:test:info", 0, -1);
        System.out.println("The current number of common messages is: " + common.size());
        System.out.println("Those common messages are:");
        for (Tuple tuple : common) {
            System.out.println(" " + tuple.getElement() + ", " + tuple.getScore());
        }
        assert common.size() >= 5;
    }

    public void logRecent(Jedis conn, String name, String message) {
        logRecent(conn, name, message, INFO);
    }

    public void logRecent(Jedis conn, String name, String message, String severity) {
        String destination = "recent:" + name + ":" + severity;
        Pipeline pipeline = conn.pipelined();
        pipeline.lpush(destination, TIMESTAMP.format(new Date()) + ' ' + message);
        pipeline.ltrim(destination, 0, 99);
        pipeline.sync();
    }

    public void logCommon(Jedis conn, String name, String message) {
        logCommon(conn, name, message, INFO, 5000);
    }

    public void logCommon(Jedis conn, String name, String message, String severity, int timeout) {
        String commonDest = "common:" + name + ':' + severity;
        String startKey = commonDest + ":start";
        long end = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < end) {
            conn.watch(startKey);
            String hourStart = ISO_FORMAT.format(new Date());
            String existing = conn.get(startKey);

            Transaction transaction = conn.multi();
            if (existing != null && COLLATOR.compare(end, hourStart) < 0) {
                transaction.rename(commonDest, commonDest + ":last");
                transaction.rename(startKey, commonDest + ":pstart");
                transaction.set(startKey, hourStart);
            }

            transaction.zincrby(commonDest, 1, message);

            String recentDest = "recent:" + name + ':' + severity;
            transaction.lpush(recentDest, TIMESTAMP.format(new Date()) + ' ' + message);
            transaction.ltrim(recentDest, 0, 99);
            List<Object> results = transaction.exec();
            if (results == null) {
                continue;
            }
            return;
        }
    }
}
