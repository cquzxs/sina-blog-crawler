package com.zxs.ssh.template.controller.thread;

import com.zxs.ssh.template.service.manage.api.ICrawlerIpService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Project Name:blog-crawler
 * File Name:ThreadIpController
 * Package Name:com.zxs.ssh.template.controller.thread
 * Date:2018/12/5
 * Author:zengxueshan
 * Description:爬取代理IP，并将代理IP存入数据库
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */

@RestController
public class ThreadIpController {

    @Resource(name = "crawlerIpService")
    private ICrawlerIpService crawlerIpService;

    /**
     * 爬取代理IP，并将代理IP存入数据库
     *
     * @return 结果
     */
    @RequestMapping("thread/crawlerIp")
    public String crawlerIp(){
        String res = "ok";
        this.crawlerIpService.crawlerIp();
        return res;
    }

    /**
     * 停止爬取代理IP
     *
     * @return 结果
     */
    @RequestMapping("thread/stopCrawlerIp")
    public String endCrawlerIp(){
        String res = "ok";
        this.crawlerIpService.stopCrawlerIp();
        return res;
    }
}
