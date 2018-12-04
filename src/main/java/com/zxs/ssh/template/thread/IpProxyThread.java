package com.zxs.ssh.template.thread;

import com.zxs.ssh.template.util.IpProxyUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Project Name:weibo-crawler
 * File Name:IpProxyThread
 * Package Name:com.zxs.ssh.template.thread
 * Date:2018/11/21
 * Author:zengxueshan
 * Description:爬取ip代理网页上的代理ip线程
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */


public class IpProxyThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(IpProxyThread.class);
    private static final LinkedBlockingQueue<String> ipProxyQueue = new LinkedBlockingQueue<>(); //代理ip队列
    private boolean isBreakdown = false; //线程是否崩溃
    private int pageCount = 100; //ip代理网页上的ip代理池共有多少页
    private int currentPage = 1; //当前页码
    private int ipCount = 0;//处理10个任务换一个ip
    private String ip = "117.191.11.79:80"; //当前使用的代理ip

    /**
     * 爬取ip代理网页上的代理ip
     */
    @Override
    public void run() {
        try{
            ipProxyQueue.put(ip);
        }catch (Exception e){
            logger.info("初始化失败");
        }
        while (!isBreakdown) {
            try {
                Thread.sleep(10 * 1000);
                System.out.println("爬取代理ip线程等待10秒");
                while (IpProxyThread.getQueueCount() > 0) {
                    if (ipCount >= 10) {
                        ip = IpProxyThread.pull(5000);
                    }
                    if (ip == null) {
                        return;
                    }
                    IpProxyUtil.setIpProxy(ip);
                    //http://ip.seofangfa.com/  https://www.kuaidaili.com/free/  http://www.89ip.cn/
                    String url = "http://www.xicidaili.com/nn/" + currentPage;
                    Document document = Jsoup.connect(url).ignoreContentType(true).timeout(200000).data().get();
                    Elements elements = document.select("tr");
                    for (int i = 1; i < elements.size(); i++) {
                        String ip = elements.get(i).select("td").get(1).html() + ":"
                                + elements.get(i).select("td").get(2).html();
                        push(ip);
                    }
                    try {
                        Elements pageCountElements = document.selectFirst(".pagination").select("a");
                        pageCount = Integer.parseInt(pageCountElements.get(pageCountElements.size() - 2).html());
                    } catch (Exception e) {
                        logger.info("获取页数异常");
                    }
                    if (ipProxyQueue.size() > 0) {
                        Thread.sleep(10 * 1000);
                    }
                    if(ipProxyQueue.size() > 10000){
                        Thread.sleep(10 * 60 * 1000);
                    }
                    if(ipProxyQueue.size() > 1000000){
                        logger.info("代理ip队列太长，可能出现只生产不消费的问题");
                        isBreakdown = true;
                    }
                    currentPage++;
                    currentPage = currentPage % pageCount;
                    ipCount++;
                }
            } catch (Exception e) {
                logger.info("爬取代理IP异常", e);
            }
        }
    }

    /**
     * 入队
     *
     * @param ip host:port
     * @throws Exception 异常
     */
    public static void push(String ip) throws Exception {
        ipProxyQueue.put(ip);
    }

    /**
     * 出队
     *
     * @param timeout 超时时间
     * @return ip
     * @throws Exception 异常
     */
    public static String pull(long timeout) throws Exception {
        return ipProxyQueue.poll(timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取队列大小
     *
     * @return 队列大小
     */
    public static int getQueueCount(){
        return ipProxyQueue.size();
    }

    public static void main(String[] args) {
        try{
            String ipTemp = "139.196.76.27:8118";
            IpProxyUtil.setIpProxy(ipTemp);
            //http://ip.seofangfa.com/  https://www.kuaidaili.com/free/  http://www.89ip.cn/
            String url = "http://www.xicidaili.com/nn/" + 1;
            Connection connection = Jsoup.connect(url);
            Document document = connection.ignoreContentType(true).ignoreHttpErrors(true).timeout(200000).data().get();
            Elements elements = document.select("tr");
            System.out.println(elements.size());
            for (int i = 1; i < elements.size(); i++) {
                String ip = elements.get(i).select("td").get(1).html() + ":"
                        + elements.get(i).select("td").get(2).html();
                System.out.println(ip);
            }
        }catch (Exception e){
            logger.info("爬取代理IP异常", e);
        }

    }
}