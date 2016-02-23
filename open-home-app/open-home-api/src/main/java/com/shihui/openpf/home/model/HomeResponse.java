/**
 * 
 */
package com.shihui.openpf.home.model;

import java.io.Serializable;

import com.shihui.openpf.common.model.Merchant;

/**
 * @author zhouqisheng
 *
 * @version 1.0 Created at: 2016年1月15日 下午2:54:15
 */
public class HomeResponse implements Serializable {

	private static final long serialVersionUID = 2801145761444410744L;
    
	private Merchant merchant;
	private int code = -1;// 查询结果
	private String msg;// 结果描述
	private String result;// 查询内容

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
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

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
