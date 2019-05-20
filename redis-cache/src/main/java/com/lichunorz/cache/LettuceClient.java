package com.lichunorz.cache;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class LettuceClient {

    private static RedisClient redisClient = RedisClient.create(RedisURI.create("redis://192.168.35.128:6379"));
    private static RedisCommands<String, String> commands;

    static {
        StatefulRedisConnection<String, String> connect = redisClient.connect();
        commands = connect.sync();
    }

    public static void put(String key, String value) {
        commands.set(key, value);
    }

    public static String get(String key) {
        return commands.get(key);
    }

    public static void main(String[] args) {


    }
}
