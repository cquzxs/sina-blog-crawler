package com.zxs.ssh.template.dao.info.api;

import com.zxs.ssh.template.model.BlogUserInfoModel;

/**
 * Project Name:blog-crawler
 * File Name:IBlogUserInfoDao
 * Package Name:com.zxs.ssh.template.dao.info.api
 * Date:2018/12/4
 * Author:zengxueshan
 * Description:博客用户信息Dao 接口
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */


public interface IBlogUserInfoDao {
    /**
     * 存储博客用户信息
     *
     * @param blogUserInfoModel 博客用户信息
     */
    void saveModel(BlogUserInfoModel blogUserInfoModel);
}
