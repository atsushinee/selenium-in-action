package com.lichunorz.weibo.driver;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.*;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Driver {

    public static final String chromeDriverPathVariableName = "webdriver.chrome.driver";
    private WebDriver webDriver;
    private Random random = new Random();

    public Driver(WebDriver driver) {
        this.webDriver = driver;
    }

    public void get(String url) {
        webDriver.get(url);
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public ChromeDriver getChromeDriver() {
        return (ChromeDriver) webDriver;
    }
    //"weibo-cookies"

    public void saveCookiesToLocal(String fileName) throws IOException {
        File cookieFile = new File(fileName);
        try (FileOutputStream fileOutputStream = new FileOutputStream(cookieFile)) {
            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
                objectOutputStream.writeObject(webDriver.manage().getCookies());
                objectOutputStream.flush();
            }
        }
    }

    public void addCookies(Set<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            try {
                webDriver.manage().addCookie(cookie);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public Set<Cookie> loadCookiesFromLocal(File file) throws IOException, ClassNotFoundException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
                final Set<Cookie> cookies = (Set<Cookie>) objectInputStream.readObject();
                return cookies;
            }
        }
    }

    public void sleep(int v) {
        try {
            Thread.sleep(v * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        webDriver.manage().timeouts().implicitlyWait(v, TimeUnit.SECONDS);
    }

    public void randomSleep() {
        try {
            Thread.sleep(random.nextInt(5) * 30 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public WebElement x(String xpathFormat, Object... args) {
        try {
            return webDriver.findElement(By.xpath(String.format(xpathFormat, args)));
        } catch (NoSuchElementException e) {
            System.err.printf(xpathFormat, args);
            System.err.println(" NoSuchElementException");
            return null;
        }
    }

    public List<WebElement> xs(String xpathFormat, Object... args) {
        return webDriver.findElements(By.xpath(String.format(xpathFormat, args)));
    }
}
