package com.wjl.blog.entity;

/**
 * 博客内容存入bean
 */
public class BlogContentBean {
    private String id;// id
    private String content; // 内容

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "BlogContentBean{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
