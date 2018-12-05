package com.zxs.ssh.template.service.manage.impl;

import com.zxs.ssh.template.service.info.api.IBlogUserInfoService;
import com.zxs.ssh.template.service.ip.api.IIpProxyService;
import com.zxs.ssh.template.service.manage.api.ICrawlerInfoService;
import com.zxs.ssh.template.thread.CrawlerFollowListThread;
import com.zxs.ssh.template.thread.CrawlerInfoThread;
import com.zxs.ssh.template.thread.QueryIpThread;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Project Name:blog-crawler
 * File Name:CrawlerInfoServiceImpl
 * Package Name:com.zxs.ssh.template.service.manage.impl
 * Date:2018/12/4
 * Author:zengxueshan
 * Description:多线程爬取用户信息Service 实现
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */

@Service("crawlerInfoService")
public class CrawlerInfoServiceImpl implements ICrawlerInfoService {

    @Resource(name = "blogUserInfoService")
    private IBlogUserInfoService blogUserInfoService;

    @Resource(name = "ipProxyService")
    private IIpProxyService ipProxyService;

    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private QueryIpThread queryIpThread;
    private List<CrawlerInfoThread> crawlerInfoThreads = new ArrayList<>();
    private CrawlerFollowListThread crawlerFollowListThread;

    /**
     * 多线程爬取用户信息
     */
    @Override
    public void crawlerInfo() {
        queryIpThread = new QueryIpThread(ipProxyService);
        new Thread(queryIpThread).start();
        for (int i = 0; i < 4; i++) {
            CrawlerInfoThread crawlerInfoThread = new CrawlerInfoThread(blogUserInfoService);
            threadPoolTaskExecutor.execute(crawlerInfoThread);
            crawlerInfoThreads.add(crawlerInfoThread);
        }
        crawlerFollowListThread = new CrawlerFollowListThread();
        new Thread(crawlerFollowListThread).start();
    }

    /**
     * 停止爬取用户信息
     */
    @Override
    public void stopCrawlerInfo() {
        if(queryIpThread != null){
            queryIpThread.setBreakdown(true);
        }
        if(crawlerInfoThreads != null && !crawlerInfoThreads.isEmpty()){
            for (int i = 0; i < crawlerInfoThreads.size(); i++) {
                crawlerInfoThreads.get(i).setBreakdown(true);
            }
        }
        if(crawlerFollowListThread != null){
            crawlerFollowListThread.setBreakdown(true);
        }
    }
}
