package com.my.learn.redisInAction;


import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by zhu on 2017/5/24.
 */
public class Chapter04Test {
    private Jedis conn;
    private Chapter04 chapter04;

    @Before
    public void beforeTest() {
        conn = new Jedis("localhost");
        chapter04 = new Chapter04();
    }

    @Test
    public void testListItem() throws Exception {
        String sellerId = "17";
        String itemId = "item1";
        String seller = "users:" + sellerId;
        String market = "market:";
        String inventory = "inventory:" + sellerId;
        conn.hset(seller, "name", "Frank");
        conn.hset(seller, "funds", "125");
        conn.sadd(inventory, itemId);
        chapter04.listItem(conn, itemId, sellerId, 34);

        Set<String> inventoryItems = conn.smembers(inventory);
        assertEquals(inventoryItems.size(), 0);
        Set<String> markets = conn.zrange("market:", 0, -1);
        Double price = conn.zscore(market, itemId + "." + sellerId);
        assertEquals(price, new Double(34.0));
        assertEquals(markets.toArray()[0], itemId + "." + sellerId);
    }

    @Test
    public void testPurchaseItem() throws Exception {

    }
}