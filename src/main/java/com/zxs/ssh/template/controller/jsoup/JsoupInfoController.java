package com.zxs.ssh.template.controller.jsoup;

import com.zxs.ssh.template.util.IpProxyUtil;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Project Name:blog-crawler
 * File Name:InfoController
 * Package Name:com.zxs.ssh.template.controller.jsoup
 * Date:2018/12/3
 * Author:zengxueshan
 * Description:通过jsoup方式爬取用户信息
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */

@RestController("jsoupInfoController")
public class JsoupInfoController {

    private static final Logger logger = LoggerFactory.getLogger(JsoupInfoController.class);

    /**
     * 通过jsoup方式爬取用户信息
     *
     * @return 用户信息
     */
    @RequestMapping("jsoup/crawlerInfo")
    public String crawlerInfo(){
        String res = "ok";
        long startTime = System.currentTimeMillis();
        IpProxyUtil.setIpProxy("219.234.5.128:3128");
        String url = "https://m.weibo.cn/api/container/getIndex?type=uid&value=1195230310&containerid=1005051195230310";
        Document document = null;
        try{
            document = Jsoup.connect(url).ignoreContentType(true).timeout(200000).data().get();
        }catch (Exception e){

        }
        long endTime = System.currentTimeMillis();
        System.out.println("请求花费时间：" + (endTime - startTime) + "ms");
        if(document != null){
            getUserInfo(document);
        }
        System.out.println("解析花费时间：" + (System.currentTimeMillis() - endTime) + "ms");
        return res;
    }

    /**
     * 获取用户信息
     *
     * @param document 文档
     */
    private void getUserInfo(Document document) {
        try{
            JSONObject jsonObject = new JSONObject(document.body().text());
            JSONObject userInfoJsonObject = jsonObject.getJSONObject("data").getJSONObject("userInfo");
            System.out.println("用户名：" + userInfoJsonObject.get("screen_name"));
            System.out.println("头像：" + userInfoJsonObject.get("avatar_hd"));
            System.out.println("介绍信息：" + userInfoJsonObject.get("verified_reason"));
            System.out.println("微博等级：" + userInfoJsonObject.get("urank"));
            System.out.println("关注数：" + userInfoJsonObject.get("follow_count"));
            System.out.println("粉丝数：" + userInfoJsonObject.get("followers_count"));
            System.out.println("微博数：" + userInfoJsonObject.get("statuses_count"));
        }catch (Exception e){
            logger.info("用户信息获取失败", e);
        }

    }

    private Map<String, String> getCookieMap(){
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
