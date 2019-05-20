package com.lichunorz.weibo;

import com.lichunorz.weibo.driver.Driver;
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
        HashMap<String, Object> map = new HashMap<>();
        System.setProperty(Driver.chromeDriverPathVariableName, "chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
        Driver app = new Driver(new ChromeDriver(chromeOptions));
        app.get(url);
        while (true) {
            //*[@id="v6_pl_content_hotfeed"]/div/div[1]/div[1]/div[2]/div[2]/a
            //*[@id="v6_pl_content_hotfeed"]/div/div[2]/div[1]/div[3]/div[2]/a
            //*[@id="v6_pl_content_hotfeed"]/div/div[3]/div[1]/div[3]/div[2]/a
            //*[@id="v6_pl_content_hotfeed"]/div/div[4]/div[1]/div[2]/div[2]/a
            try {
                app.sleep(2);
                app.x("//*[@id=\"v6_pl_content_hottips\"]/div[2]/div[1]/div/ul/li[2]/a").click();
                app.sleep(2);
                List<WebElement> hotDiv = app.xs("//*[@id=\"v6_pl_content_hotfeed\"]/div/div");

                System.out.println(hotDiv.size());
                for (int i = 1; i <= hotDiv.size(); i++) {
                    System.out.printf("=================================第 %d 条=======================================\n", i);

                    try {
                        String title = app.x("(//*[@class=\"W_f14 W_fb S_txt1\"])[%d]", i).getText();
                        String time = app.x("//*[@class=\"WB_detail\"]/*[@class=\"WB_from S_txt2\"]/a[1]", i).getText();
                        String content = app.x("(//*[@class=\"WB_text W_f14\"])[%d]", i).getText();

                        System.out.printf("【%s】【%s】:\n%s\n", title, time, content);

                        if (map.containsKey(title + time)) {
                            System.err.println("已经回复过了");
                            continue;
                        }
                        System.out.println("是否关注?");
                        final WebElement elemFollow = app.x("(//*[@class=\"W_btn_b btn_22px\"])[%d]", i);
                        if (elemFollow == null) {
                            System.err.println("未找到关注按钮");
                        } else {
                            final String followActionType = elemFollow.getAttribute("action-type");
                            if (followActionType != null && followActionType.equals("follow")) {
                                elemFollow.click();
                                System.out.println("该用户未关注，当前已关注");
                            } else {
                                System.out.println("已经成功关注");
                            }
                        }

//                        String reply = parseContent(content);
//
//                        app.x("//*[@id=\"v6_pl_content_hotfeed\"]/div/div[%d]/div[2]/div/ul/li[3]/a", i).click();
//                        app.sleep(2);
//                        final String info = app.x("//*[@id=\"v6_pl_content_hotfeed\"]/div/div[%d]/div[3]/div/div/div[1]/p[2]", i)
//                                .getText();
//                        if (info.contains("无法回复")) {
//                            System.err.println(info);
//                            continue;
//                        }
//
//                        app.x("//*[@id=\"v6_pl_content_hotfeed\"]/div/div[%d]/div[3]/div/div/div[2]/div[2]/div[1]/textarea", i)
//                                .sendKeys(reply);
//                        app.x("//*[@id=\"v6_pl_content_hotfeed\"]/div/div[%d]/div[3]/div/div/div[2]/div[2]/div[2]/div[1]/a", i).click();
//                        app.sleep(1);
//                        final WebElement layer = app.x("//*[@class=\"W_layer \"]");
//                        if (layer != null) {
//                            System.err.println("评论失败:" + app.x("//*[@class=\"W_layer \"]/div[2]/div[3]/div/dl/dd/div").getText());
//                            break;
//                        }
//                        System.out.println("【评论内容】:");
//                        System.out.println(reply);
                        map.put(title + time, object);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.err.println("评论失败:" + ex.getMessage());
                        continue;
                    }

                    app.sleep(1);

                }
                System.out.println("======================= 该页已全部评论结束 =========================");
//                app.randomSleep();
                app.sleep(2);
                System.out.println("======================= 重新刷新 =========================");
                app.getChromeDriver().navigate().refresh();
            } catch (Exception ex) {
                System.err.println("===============error:" + ex.getMessage());
            }
        }

    }

    private static String parseContent(String content) {
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
//        text += "--有好心人在我的第一条微博下面回复一下吗[沙雕],[感谢][感谢]";
        return text;
    }
}

