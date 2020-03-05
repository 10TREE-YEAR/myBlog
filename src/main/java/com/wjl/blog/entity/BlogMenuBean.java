package com.wjl.blog.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 博客后台菜单bean
 */
@Data
public class BlogMenuBean implements Serializable {

    private String id; // 菜单id
    private String name; // 菜单名称
    private String url; // 菜单跳转链接
    private String firstJB; // 一级菜单
    private String twoJB; // 二级菜单
    private String treeJB; // 三级菜单
    private String start; //状态
    private String sort; // 排序
    private String menuDesc; //备注
    private String menuTime; // 创建时间
    private String menuStyle;// 菜单样式
}
