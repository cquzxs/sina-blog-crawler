package com.zxs.ssh.template.util;

/**
 * Project Name:blog-crawler
 * File Name:IpProxyUtil
 * Package Name:com.zxs.ssh.template.util
 * Date:2018/12/3
 * Author:zengxueshan
 * Description:
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */


public class IpProxyUtil {
    /**
     * 设置代理ip
     *
     * @param ip host:port
     */
    public static void setIpProxy(String ip){
        System.setProperty("http.maxRedirects", "50");
        System.getProperties().setProperty("proxySet", "true");
        // 如果不设置，只要代理IP和代理端口正确,此项不设置也可以
        System.getProperties().setProperty("http.proxyHost", ip.split(":")[0]);
        System.getProperties().setProperty("http.proxyPort", ip.split(":")[1]);
    }
}
