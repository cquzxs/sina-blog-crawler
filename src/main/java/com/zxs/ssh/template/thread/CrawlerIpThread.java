package com.zxs.ssh.template.thread;

import com.zxs.ssh.template.model.IpProxyModel;
import com.zxs.ssh.template.model.Queue.IpQueue;
import com.zxs.ssh.template.service.ip.api.IIpProxyService;
import com.zxs.ssh.template.util.IpProxyUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.List;

/**
 * Project Name:weibo-crawler
 * File Name:CrawlerIpThread
 * Package Name:com.zxs.ssh.template.thread
 * Date:2018/11/21
 * Author:zengxueshan
 * Description:爬取ip代理网页上的代理ip线程
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */


public class CrawlerIpThread implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(CrawlerIpThread.class);
    private boolean isBreakdown = false; //线程是否崩溃
    private static final int MAX_IP_COUNT = 100;//每个IP处理任务的最大值
    private static final long QUEUE_PULL_TIME_OUT = 5000;//出队超时时间
    private int pageCount = 100; //ip代理网页上的ip代理池共有多少页
    private int currentPage = 1; //当前页码
    private int ipCount = 0;//处理10个任务换一个ip
    private String ip = "119.180.177.245:8060"; //当前使用的代理ip

    private IIpProxyService ipProxyService;

    public CrawlerIpThread(IIpProxyService ipProxyService) {
        this.ipProxyService = ipProxyService;
    }

    /**
     * 爬取ip代理网页上的代理ip
     */
    @Override
    public void run() {
        try {
            IpProxyModel ipProxyModel = new IpProxyModel();
            ipProxyModel.setIp(ip);
            IpQueue.push(ipProxyModel);
            List<IpProxyModel> ipProxyModels = this.ipProxyService.queryModels(0,100);
            if(ipProxyModels != null && !ipProxyModels.isEmpty()){
                for (int i = 0; i < ipProxyModels.size(); i++) {
                    IpQueue.push(ipProxyModels.get(i));
                }
            }
        } catch (Exception e) {
            logger.info("初始化失败");
        }
        while (!isBreakdown) {
            try {
                Thread.sleep(10 * 1000);
                while (IpQueue.getQueueCount() > 0 && !isBreakdown) {
                    if (ipCount >= MAX_IP_COUNT) {
                        ip = IpQueue.pull(QUEUE_PULL_TIME_OUT).getIp();
                    }
                    if (ip == null) {
                        return;
                    }
                    IpProxyUtil.setIpProxy(ip);
                    //http://ip.seofangfa.com/  https://www.kuaidaili.com/free/  http://www.89ip.cn/ http://www.ip3366.net/
                    String url = "http://www.xicidaili.com/nn/" + currentPage;
                    Document document = Jsoup.connect(url).userAgent("Mozilla").ignoreHttpErrors(true).timeout(200000).data().get();
                    Elements elements = document.select("tr");
                    for (int i = 1; i < elements.size(); i++) {
                        IpProxyModel ipProxyModel = new IpProxyModel();
                        String ip = elements.get(i).select("td").get(1).html() + ":"
                                + elements.get(i).select("td").get(2).html();
                        ipProxyModel.setIp(ip);
                        ipProxyModel.setLocation(elements.get(i).select("td").get(3).html());
                        ipProxyModel.setCreateTime(new Timestamp(System.currentTimeMillis()));
                        ipProxyModel.setUpdateTime(new Timestamp(System.currentTimeMillis()));
                        long responseTime = checkUseful(ip);
                        if (responseTime == -1) {
                            continue;
                        }
                        ipProxyModel.setResponseSpeed(responseTime + "ms");
                        this.ipProxyService.saveModel(ipProxyModel);
                        IpQueue.push(ipProxyModel);
                    }
                    try {
                        Elements pageCountElements = document.selectFirst(".pagination").select("a");
                        pageCount = Integer.parseInt(pageCountElements.get(pageCountElements.size() - 2).html());
                    } catch (Exception e) {
                        logger.info("获取页数异常");
                    }
                    if (IpQueue.getQueueCount() > 0) {
                        Thread.sleep(10 * 1000);
                    }
                    if (IpQueue.getQueueCount() > 10000) {
                        Thread.sleep(10 * 60 * 1000);
                    }
                    if (IpQueue.getQueueCount() > 1000000) {
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
        logger.info("爬取代理ip线程已停止");
    }

    /**
     * 测试IP是否可用
     *
     * @param ip host:port
     * @return ip响应时间
     */
    private long checkUseful(String ip) {
        long res = -1;
        try {
            String urlTest = "https://www.example.com/";
            IpProxyUtil.setIpProxy(ip);
            long startTime = System.currentTimeMillis();
            Connection connection = Jsoup.connect(urlTest)
                    .userAgent("Mozilla")
                    .ignoreHttpErrors(true)
                    .timeout(200000)
                    .data();
            connection.get();
            if (connection.response().statusCode() == 200) {
                long endTime = System.currentTimeMillis();
                res = endTime - startTime;
            } else {
                res = -1;
            }
        } catch (Exception e) {
            res = -1;
        }
        return res;
    }

    public void setBreakdown(boolean breakdown) {
        isBreakdown = breakdown;
    }
}