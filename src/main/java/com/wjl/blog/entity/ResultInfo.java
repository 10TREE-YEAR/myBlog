package com.wjl.blog.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 返回信息公共类
 */
@Data
public class ResultInfo implements Serializable {

    private String resultMsg; // 返回信息

    private String resultCode;// 返回码

    private String resultContent;// 返回内容

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultContent() {
        return resultContent;
    }

    public void setResultContent(String resultContent) {
        this.resultContent = resultContent;
    }

    @Override
    public String toString() {
        return "ResultInfo{" +
                "resultMsg='" + resultMsg + '\'' +
                ", resultCode='" + resultCode + '\'' +
                ", resultContent='" + resultContent + '\'' +
                '}';
    }
}
