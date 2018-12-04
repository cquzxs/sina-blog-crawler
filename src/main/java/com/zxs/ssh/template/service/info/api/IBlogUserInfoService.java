package com.zxs.ssh.template.service.info.api;

import com.zxs.ssh.template.model.BlogUserInfoModel;

/**
 * Project Name:blog-crawler
 * File Name:IBlogUserInfoService
 * Package Name:com.zxs.ssh.template.service.info.api
 * Date:2018/12/4
 * Author:zengxueshan
 * Description:博客用户信息Service 接口
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */


public interface IBlogUserInfoService {
    /**
     * 存储博客用户信息
     *
     * @param blogUserInfoModel 博客用户信息
     */
    void saveModel(BlogUserInfoModel blogUserInfoModel);
}
