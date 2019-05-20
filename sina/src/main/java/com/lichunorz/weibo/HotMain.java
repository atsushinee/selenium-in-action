package com.lichunorz.weibo;

import com.lichunorz.weibo.cache.RedisCache;
import com.lichunorz.weibo.driver.Driver;
import com.lichunorz.weibo.weibo.IWeiboCache;
import com.lichunorz.weibo.weibo.Weibo;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.util.*;

public class HotMain {
    private final static String url = "https://weibo.com/";
    private final static Object object = null;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        Runtime run = Runtime.getRuntime();
//        Process process = run.exec(chromePath + " --remote-debugging-port=9222 --user-data-dir=\"C:\\selenum\\AutomationProfile");
//        System.setProperty(Driver.chromeDriverPathVariableName, "chromedriver.exe");
        Weibo weibo = new Weibo();
        weibo.setCache(new RedisCache());
        weibo.run();
    }
}

