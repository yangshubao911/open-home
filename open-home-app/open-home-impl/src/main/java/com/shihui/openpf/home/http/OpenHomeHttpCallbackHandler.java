/**
 * 
 */
package com.shihui.openpf.home.http;

import com.shihui.openpf.common.model.Merchant;
import com.shihui.openpf.home.api.ResultParser;

/**
 * @author zhouqisheng
 * @param <T>
 *
 */
public class OpenHomeHttpCallbackHandler<T> extends HttpCallbackHandler<T> {
	private ResultParser resultParser;
	private Merchant merchant;

	public OpenHomeHttpCallbackHandler(Merchant merchant, ResultParser resultParser) {
		this.merchant = merchant;
		this.resultParser = resultParser;
	}

	public ResultParser getResultParser() {
		return resultParser;
	}

	public Merchant getMerchant() {
		return merchant;
	}

}
