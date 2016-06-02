package com.shihui.openpf.home.model;

import java.io.Serializable;

/**
 * Created by zhoutc on 2015/10/26.
 */
public class YjzUpdateResult implements Serializable{
	private static final long serialVersionUID = -5645093124076183513L;
	
	private int code;
    private String msg;
    private String[] body;

    public YjzUpdateResult(int code, String msg, String[] body) {
        this.code = code;
        this.msg = msg;
        this.body = body;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String[] getBody() {
        return body;
    }

    public void setBody(String[] body) {
        this.body = body;
    }
}
