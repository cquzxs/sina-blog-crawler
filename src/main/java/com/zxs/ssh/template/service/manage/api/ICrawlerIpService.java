package com.zxs.ssh.template.service.manage.api;

/**
 * Project Name:blog-crawler
 * File Name:ICrawlerIpService
 * Package Name:com.zxs.ssh.template.service.manage.api
 * Date:2018/12/5
 * Author:zengxueshan
 * Description:多线程爬取代理IP Service 接口
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */


public interface ICrawlerIpService {

    /**
     * 多线程爬取代理IP
     */
    void crawlerIp();

    /**
     * 停止爬取代理IP
     */
    void endCrawlerIp();

}
