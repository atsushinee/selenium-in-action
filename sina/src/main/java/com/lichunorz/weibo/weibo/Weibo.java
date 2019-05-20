package com.lichunorz.weibo.weibo;

import com.lichunorz.weibo.driver.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;
import java.util.Random;

public class Weibo {
    private final static String url = "https://weibo.com/";
    private final static String defaultDebugAddress = "127.0.0.1:9222";

    private Driver driver;
    private IWeiboComment weiboComment;
    private IWeiboCache weiboCache;

    public Weibo(String debugAddress, IWeiboComment weiboComment) {
        init(debugAddress, weiboComment);
    }

    public void setCache(IWeiboCache weiboCache) {
        this.weiboCache = weiboCache;
    }

    public Weibo() {
        init(defaultDebugAddress, new DefaultWeiboComment());
    }

    public Weibo(String debugAddress) {
        init(debugAddress, new DefaultWeiboComment());
    }


    private void init(String debugAddress, IWeiboComment weiboComment) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("debuggerAddress", debugAddress);
        this.driver = new Driver(new ChromeDriver(chromeOptions));
        this.weiboComment = weiboComment;
    }

    public void run() {
        driver.get(url);
        while (true) {
            try {
                driver.sleep(2);
                driver.x("//*[@id=\"v6_pl_content_hottips\"]/div[2]/div[1]/div/ul/li[2]/a").click();
                driver.sleep(2);
                List<WebElement> hotDiv = driver.xs("//*[@id=\"v6_pl_content_hotfeed\"]/div/div");

                System.out.println(hotDiv.size());
                for (int i = 1; i <= hotDiv.size(); i++) {
                    System.out.printf("=================================第 %d 条=======================================\n", i);

                    try {
                        driver.sleep(1);

                        String title = driver.x("(//*[@class=\"W_f14 W_fb S_txt1\"])[%d]", i).getText();
                        String time = driver.x("//*[@class=\"WB_detail\"]/*[@class=\"WB_from S_txt2\"]/a[1]", i).getText();
                        String content = driver.x("(//*[@class=\"WB_text W_f14\"])[%d]", i).getText();

                        System.out.printf("【%s】【%s】:\n%s\n", title, time, content);

                        if (weiboCache != null) {
                            if (weiboCache.get(title + time) != null) {
                                System.err.println("已经回复过了");
                                continue;
                            }
                        }
//                        System.out.println("是否关注?");
//                        final WebElement elemFollow = driver.x("(//*[@class=\"W_btn_b btn_22px\"])[%d]", i);
//                        if (elemFollow == null) {
//                            System.err.println("未找到关注按钮");
//                        } else {
//                            final String followActionType = elemFollow.getAttribute("action-type");
//                            if (followActionType != null && followActionType.equals("follow")) {
//                                elemFollow.click();
//                                System.out.println("该用户未关注，当前已关注");
//                            } else {
//                                System.out.println("已经成功关注");
//                            }
//                        }

                        String reply = weiboComment.comment(content);

                        driver.x("//*[@id=\"v6_pl_content_hotfeed\"]/div/div[%d]/div[2]/div/ul/li[3]/a", i).click();
                        driver.sleep(2);
                        final String info = driver.x("//*[@id=\"v6_pl_content_hotfeed\"]/div/div[%d]/div[3]/div/div/div[1]/p[2]", i)
                                .getText();
                        if (info.contains("无法回复")) {
                            System.err.println(info);
                            continue;
                        }

                        driver.x("//*[@id=\"v6_pl_content_hotfeed\"]/div/div[%d]/div[3]/div/div/div[2]/div[2]/div[1]/textarea", i)
                                .sendKeys(reply);
                        driver.x("//*[@id=\"v6_pl_content_hotfeed\"]/div/div[%d]/div[3]/div/div/div[2]/div[2]/div[2]/div[1]/a", i).click();
                        driver.sleep(1);
                        final WebElement layer = driver.x("//*[@class=\"W_layer \"]");
                        if (layer != null) {
                            System.err.println("评论失败:" + driver.x("//*[@class=\"W_layer \"]/div[2]/div[3]/div/dl/dd/div").getText());
                            break;
                        }
                        System.out.println("【评论内容】:");
                        System.out.println(reply);
                        if (weiboCache != null) {
                            weiboCache.put(title + time, "1");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.err.println("评论失败:" + ex.getMessage());
                        continue;
                    }

                    driver.sleep(1);

                }
                System.out.println("======================= 该页已全部评论结束 =========================");
//                app.randomSleep();
                driver.sleep(2);
                System.out.println("======================= 重新刷新 =========================");
                driver.getChromeDriver().navigate().refresh();
            } catch (Exception ex) {
                System.err.println("===============error:" + ex.getMessage());
            }
        }
    }

}
