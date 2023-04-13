package com.xuecheng.base.exception;

import java.io.Serializable;

/**
 * @author xb
 * @description 和前端约定返回的异常信息模型
 * @create 2023-03-22 14:38
 * @vesion 1.0
 */
public class RestErrorResponse implements Serializable {
    private String errMessage;

    public RestErrorResponse(String errMessage){
        this.errMessage= errMessage;
    }
    public String getErrMessage() {
        return errMessage;
    }
    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }


}
