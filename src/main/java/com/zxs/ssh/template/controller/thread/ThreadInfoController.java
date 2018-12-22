package com.zxs.ssh.template.controller.thread;

import com.zxs.ssh.template.service.manage.api.ICrawlerInfoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Project Name:blog-crawler
 * File Name:ThreadInfoController
 * Package Name:com.zxs.ssh.template.controller.thread
 * Date:2018/12/4
 * Author:zengxueshan
 * Description:多线程爬取用户信息
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */

@RestController("threadInfoController")
public class ThreadInfoController {

    @Resource(name = "crawlerInfoService")
    private ICrawlerInfoService crawlerInfoService;

    private static boolean isRunning = false;

    /**
     * 多线程爬取用户信息
     *
     * @return 用户信息
     */
    @RequestMapping("thread/crawlerInfo")
    public String crawlerInfo(){
        String res = "ok";
        if(isRunning){
            return res;
        }
        this.crawlerInfoService.crawlerInfo();
        isRunning = true;
        return res;
    }

    /**
     * 停止多线程爬取用户信息
     *
     * @return 用户信息
     */
    @RequestMapping("thread/stopCrawlerInfo")
    public String stopCrawlerInfo(){
        String res = "ok";
        if(!isRunning){
            return res;
        }
        this.crawlerInfoService.stopCrawlerInfo();
        isRunning = false;
        return res;
    }

}
