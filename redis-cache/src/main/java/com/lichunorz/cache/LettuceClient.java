package com.lichunorz.cache;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class LettuceClient {

    private static RedisCommands<String, String> commands;
    private static final String url = "redis://192.168.1.99:6379";

    private static RedisCommands<String, String> getCommands() {
        if (commands == null) {
            RedisClient redisClient = RedisClient.create(RedisURI.create(url));
            StatefulRedisConnection<String, String> connect = redisClient.connect();
            commands = connect.sync();
        }
        return commands;
    }

    public static void set(String key, String value) {
        LettuceClient.getCommands().set(key, value);
    }

    public static boolean setnx(String key, String value) {
        return LettuceClient.getCommands().setnx(key, value);
    }

    public static String get(String key) {
        return LettuceClient.getCommands().get(key);
    }


    public static void main(String[] args) {

        LettuceClient.getCommands().set("10", "11");
        System.out.println(LettuceClient.getCommands().get("10"));
    }
}
