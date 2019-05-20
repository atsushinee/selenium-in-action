package com.lichunorz.weibo;

import com.lichunorz.weibo.cache.RedisCache;
import com.lichunorz.weibo.driver.Driver;
import com.lichunorz.weibo.provider.QiuBaiCommentProvider;
import com.lichunorz.weibo.weibo.Weibo;

import java.io.IOException;

public class SinaApplication {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        Runtime run = Runtime.getRuntime();
//        Process process = run.exec(chromePath + " --remote-debugging-port=9222 --user-data-dir=\"C:\\selenum\\AutomationProfile");
        System.setProperty(Driver.chromeDriverPathVariableName, "C:\\Users\\atsus\\Desktop\\chromedriver_win32\\chromedriver.exe");
        Weibo weibo = new Weibo();
        weibo.setCache(new RedisCache());
        weibo.setCommentProvider(new QiuBaiCommentProvider());
        weibo.run();
    }
}

