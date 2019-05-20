package com.lichunorz.weibo.weibo;

public interface IWeiboCache {

    void put(String key, String value);

    String get(String key);
}
