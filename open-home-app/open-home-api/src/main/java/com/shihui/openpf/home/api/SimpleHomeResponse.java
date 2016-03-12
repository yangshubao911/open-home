/**
 * 
 */
package com.shihui.openpf.home.api;

import java.io.Serializable;

/**
 * @author zhouqisheng
 * @date 2016年3月11日 下午2:02:54
 *
 */
public class SimpleHomeResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private int code;// 0-成功，1-失败
	private String msg;

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

	@Override
	public String toString() {
		return "SimpleHomeResponse [code=" + code + ", msg=" + msg + "]";
	}
}
