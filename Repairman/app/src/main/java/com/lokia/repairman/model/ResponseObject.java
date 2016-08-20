package com.lokia.repairman.model;

import org.springframework.util.StringUtils;

/**
 * Created by lokia on 16-8-20.
 */
public class ResponseObject<T>{

    private T content;

    private  String msg;

    private  Integer resultCode;

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

   public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public boolean isEmpty() {
        boolean isContentEmpty = content == null;
        boolean isMsgEmpty = !StringUtils.hasText(msg);
        boolean isResultCodeEmpty = resultCode == null;

        return isContentEmpty && isMsgEmpty && isResultCodeEmpty;

    }
}
