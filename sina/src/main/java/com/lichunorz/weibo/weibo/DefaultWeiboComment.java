package com.lichunorz.weibo.weibo;

public class DefaultWeiboComment implements IWeiboComment {
    @Override
    public String comment(String content) {
        String text;
        if (content.length() >= 31) {
            if (content.contains("。")) {
                text = content.split("。")[0];
            } else if (content.contains("!")) {
                text = content.split("!")[0];
            } else if (content.contains("！")) {
                text = content.split("！")[0];
            } else {
                text = content.substring(0, 30);
            }
        } else {
            text = content;
        }
        return text;
    }
}
