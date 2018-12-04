package com.zxs.ssh.template.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Project Name:blog-crawler
 * File Name:BlogUserInfoModel
 * Package Name:com.zxs.ssh.template.model
 * Date:2018/12/4
 * Author:zengxueshan
 * Description:博客用户信息Model
 * Copyright (c) 2018, 重庆云凯科技有限公司 All Rights Reserved.
 */

@Entity
@Table(name = "blog_user_info", schema = "blog-crawler")
public class BlogUserInfoModel {

    private String id; //主键id
    private String userId; //用户id
    private String username; //用户名
    private String avatar; //头像
    private String introduce; //介绍信息
    private int blogLevel; //微博等级
    private int followCount; //关注数
    private int fansCount; //粉丝数
    private int blogCount; //博客数
    private Timestamp createTime;//创建时间
    private Timestamp updateTime; //更新时间


    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "user_id")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "avatar")
    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Basic
    @Column(name = "introduce")
    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    @Basic
    @Column(name = "blog_level")
    public int getBlogLevel() {
        return blogLevel;
    }

    public void setBlogLevel(int blogLevel) {
        this.blogLevel = blogLevel;
    }

    @Basic
    @Column(name = "follow_count")
    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }

    @Basic
    @Column(name = "fans_count")
    public int getFansCount() {
        return fansCount;
    }

    public void setFansCount(int fansCount) {
        this.fansCount = fansCount;
    }

    @Basic
    @Column(name = "blog_count")
    public int getBlogCount() {
        return blogCount;
    }

    public void setBlogCount(int blogCount) {
        this.blogCount = blogCount;
    }

    @Basic
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "update_time")
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlogUserInfoModel that = (BlogUserInfoModel) o;

        if (blogLevel != that.blogLevel) return false;
        if (followCount != that.followCount) return false;
        if (fansCount != that.fansCount) return false;
        if (blogCount != that.blogCount) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (avatar != null ? !avatar.equals(that.avatar) : that.avatar != null) return false;
        if (introduce != null ? !introduce.equals(that.introduce) : that.introduce != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        return updateTime != null ? updateTime.equals(that.updateTime) : that.updateTime == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (avatar != null ? avatar.hashCode() : 0);
        result = 31 * result + (introduce != null ? introduce.hashCode() : 0);
        result = 31 * result + blogLevel;
        result = 31 * result + followCount;
        result = 31 * result + fansCount;
        result = 31 * result + blogCount;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BlogUserInfoModel{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", avatar='" + avatar + '\'' +
                ", introduce='" + introduce + '\'' +
                ", blogLevel=" + blogLevel +
                ", followCount=" + followCount +
                ", fansCount=" + fansCount +
                ", blogCount=" + blogCount +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
