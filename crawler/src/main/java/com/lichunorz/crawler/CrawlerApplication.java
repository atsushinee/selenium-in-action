package com.lichunorz.crawler;

import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlerApplication {
    private static final LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        OkHttpClient okHttpClient = new OkHttpClient();
        int pageCount = 13;
        ExecutorService executor = Executors.newFixedThreadPool(pageCount);


        for (int i = 1; i <= pageCount; i++) {
            final int page = i;
            executor.execute(() -> {
                Request request = new Request.Builder().url("https://www.qiushibaike.com/text/page/" + page + "/").build();
                Call call = okHttpClient.newCall(request);
                try (Response response = call.execute()) {
                    int code = response.code();
                    if (code == 200) {
                        ResponseBody body = response.body();
                        if (body != null) {
                            String content = body.string();
                            System.out.println(content);
                            Pattern pattern = Pattern.compile("<div class=\"content\">\\s<span>(\\s*.*\\s*)</span>");
                            Matcher matcher = pattern.matcher(content);
                            while (matcher.find()) {
                                System.out.println(matcher.group(1));
                            }

                        }
                    } else {
                        System.err.println("Response response = call.execute() ==== > code ==== >" + code);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        }

    }
}
