package com.lichunorz.weibo.provider;

import com.lichunorz.cache.LettuceClient;
import com.lichunorz.weibo.weibo.IWeiboComment;

import java.util.Random;

public class QiuBaiCommentProvider implements IWeiboComment {
    private Random random = new Random();

    @Override
    public String comment(String content) {
        String v = LettuceClient.get("qubai:" + random.nextInt(302));
        if (v == null || v.length() > 140) {
            return comment(null);
        } else {
            return v;
        }
    }
}
