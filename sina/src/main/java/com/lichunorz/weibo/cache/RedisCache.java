package com.lichunorz.weibo.cache;

import com.lichunorz.cache.LettuceClient;
import com.lichunorz.weibo.weibo.IWeiboCache;

public class RedisCache implements IWeiboCache {
    private final static String prefix = "weibo:";

    @Override
    public void put(String key, String value) {
        LettuceClient.put(prefix + key, value);
    }

    @Override
    public String get(String key) {
        return LettuceClient.get(prefix + key);
    }
}
