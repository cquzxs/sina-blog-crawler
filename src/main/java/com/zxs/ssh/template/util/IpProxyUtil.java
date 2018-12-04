package com.zxs.ssh.template.util;

import java.util.HashMap;
import java.util.Map;

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

    /**
     * 获取cookie信息
     *
     * @return cookie信息
     */
    public static Map<String, String> getCookieMap(){
        Map<String, String> map=new HashMap<>();
        map.put("_s_tentry","login.sina.com.cn");
        map.put("Apache","3072989182787.392.1480858059338");
        map.put("login_sid_t","89596622af1c55ce27d4e2014ce266f2");
        map.put("SCF","Am1Px4Tom_6UrbPEEMj1BFx_b9I_R-SIe79a8uwyZZ1wAq1G9lz70IGe372BGUn-1P2iSOpnpHs1mG__cljsA08.");
        map.put("SINAGLOBAL","1793024353389.4553.1479174837212");
        map.put("SUB","_2AkMvGLRGdcNhrABVnfEXxG7maI1H-jzEiebBAn7uJhMyAxh77goMqSWXdZhKmkaeRY4olOi-mJAYtV-Mzg..");
        map.put("SUBP","0033WrSXqPxfM72wWs9jqgMF55529P9D9WhiexOd4VLhUIk_kVInXZ9I5JpV2heRe02E1K.cSXWpMC4odcXt");
        map.put("SUHB","0qjkCp7v-5C29u");
        map.put("ULV","1480858059441:4:2:2:3072989182787.392.1480858059338:1480834549292");
        map.put("un","383129967@qq.com");
        map.put("UOR","s.share.baidu.com,service.weibo.com,login.sina.com.cn");
        map.put("TC-Page-G0","cdcf495cbaea129529aa606e7629fea7");
        map.put("TC-Ugrow-G0","e66b2e50a7e7f417f6cc12eec600f517");
        map.put("TC-V5-G0","b8dff68fa0e04b3c8f0ba710d783479a");
        map.put("wb_bub_find","1");
        map.put("wb_bub_find_3459285440","1");
        map.put("wb_bub_find_5601838195","1");
        map.put("WBStorage","2c466cc84b6dda21|undefined");
        return map;
    }
}
