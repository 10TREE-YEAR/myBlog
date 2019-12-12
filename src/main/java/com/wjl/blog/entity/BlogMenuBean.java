package com.wjl.blog.entity;

import java.io.Serializable;

/**
 * 博客后台菜单bean
 */
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

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFirstJB() {
        return firstJB;
    }

    public void setFirstJB(String firstJB) {
        this.firstJB = firstJB;
    }

    public String getTwoJB() {
        return twoJB;
    }

    public void setTwoJB(String twoJB) {
        this.twoJB = twoJB;
    }

    public String getTreeJB() {
        return treeJB;
    }

    public void setTreeJB(String treeJB) {
        this.treeJB = treeJB;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getMenuDesc() {
        return menuDesc;
    }

    public void setMenuDesc(String menuDesc) {
        this.menuDesc = menuDesc;
    }

    public String getMenuTime() {
        return menuTime;
    }

    public void setMenuTime(String menuTime) {
        this.menuTime = menuTime;
    }
}
