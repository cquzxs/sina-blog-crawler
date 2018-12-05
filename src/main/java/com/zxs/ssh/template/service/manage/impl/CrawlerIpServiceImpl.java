package com.zxs.ssh.template.service.manage.impl;

import com.zxs.ssh.template.service.ip.api.IIpProxyService;
import com.zxs.ssh.template.service.manage.api.ICrawlerIpService;
import com.zxs.ssh.template.thread.CrawlerIpThread;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Project Name:blog-crawler
 * File Name:CrawlerIpServiceImpl
 * Package Name:com.zxs.ssh.template.service.manage.impl
 * Date:2018/12/5
 * Author:zengxueshan
 * Description:多线程爬取代理IP Service 实现
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */

@Service("crawlerIpService")
public class CrawlerIpServiceImpl implements ICrawlerIpService{

    @Resource(name = "ipProxyService")
    private IIpProxyService ipProxyService;
    private CrawlerIpThread ipProxyThread;

    /**
     * 多线程爬取代理IP
     */
    @Override
    public void crawlerIp() {
        ipProxyThread = new CrawlerIpThread(ipProxyService);
        new Thread(ipProxyThread).start();
    }

    /**
     * 停止爬取代理IP
     */
    @Override
    public void endCrawlerIp() {
        if(ipProxyThread != null){
            ipProxyThread.setBreakdown(true);
        }
    }
}
