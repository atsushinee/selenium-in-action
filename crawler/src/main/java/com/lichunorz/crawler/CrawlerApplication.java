package com.lichunorz.crawler;

import com.lichunorz.cache.LettuceClient;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrawlerApplication {
    //    private static final LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
    private static final String prefix = "qubai:";

    public static void main(String[] args) {
        OkHttpClient okHttpClient = new OkHttpClient();
        int pageCount = 13;
        ExecutorService executor = Executors.newFixedThreadPool(pageCount);

        final String qubaiCountStr = LettuceClient.get(prefix + "count");
        AtomicInteger count = new AtomicInteger(qubaiCountStr == null ? 0 : Integer.parseInt(qubaiCountStr));
        for (int i = 1; i <= pageCount; i++) {
            final int page = i;
            executor.execute(() -> {
                System.out.println("第 " + page + " 组 开始");
                Request request = new Request.Builder().url("https://www.qiushibaike.com/text/page/" + page + "/").build();
                Call call = okHttpClient.newCall(request);
                try (Response response = call.execute()) {
                    int code = response.code();
                    if (code == 200) {
                        ResponseBody body = response.body();
                        if (body != null) {
                            String content = body.string();
                            Pattern pattern = Pattern.compile("<div class=\"content\">\\s<span>(\\s*.*\\s*)</span>");
                            Matcher matcher = pattern.matcher(content);
                            while (matcher.find()) {
                                String v = matcher.group(1).trim().replaceAll("</*[a-zA-z]+/*>", " ");
                                while (!LettuceClient.setnx(prefix + count.getAndIncrement(), v)) {

                                }


                            }

                        }
                    } else {
                        System.err.println("Response response = call.execute() ==== > code ==== >" + code);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("第 " + page + " 组 结束");

            });

        }
        executor.shutdown();

    }
}
