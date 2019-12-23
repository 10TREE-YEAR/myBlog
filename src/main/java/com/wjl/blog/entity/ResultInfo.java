package com.wjl.blog.entity;

import lombok.Data;

/**
 * 返回信息公共类
 */
@Data
public class ResultInfo {

    private String resultMsg; // 返回信息

    private String resultCode;// 返回码

    private String resultContent;// 返回内容


}
