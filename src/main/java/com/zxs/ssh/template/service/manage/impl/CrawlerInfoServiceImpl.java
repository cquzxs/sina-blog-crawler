package com.zxs.ssh.template.service.manage.impl;

import com.zxs.ssh.template.service.info.api.IBlogUserInfoService;
import com.zxs.ssh.template.service.manage.api.ICrawlerInfoService;
import com.zxs.ssh.template.thread.CrawlerFollowListThread;
import com.zxs.ssh.template.thread.CrawlerInfoThread;
import com.zxs.ssh.template.thread.IpProxyThread;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
public class CrawlerInfoServiceImpl implements ICrawlerInfoService{

    @Resource(name = "blogUserInfoService")
    private IBlogUserInfoService blogUserInfoService;

    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 多线程爬取用户信息
     */
    @Override
    public void crawlerInfo() {
        IpProxyThread ipProxyThread = new IpProxyThread();
        new Thread(ipProxyThread).start();
        for (int i = 0; i < 4; i++) {
            CrawlerInfoThread crawlerInfoThread = new CrawlerInfoThread(blogUserInfoService);
            threadPoolTaskExecutor.execute(crawlerInfoThread);
        }
        CrawlerFollowListThread crawlerFollowListThread = new CrawlerFollowListThread();
        new Thread(crawlerFollowListThread).start();
    }
}
