package com.nft.config.xss;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import cn.hutool.core.util.EscapeUtil;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

	public XssHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getHeader(String name) {
		String value = super.getHeader(name);
		if (value != null) {
			value = EscapeUtil.escapeHtml4(value);
		}
		return value;
	}

	@Override
	public String getQueryString() {
		String value = super.getQueryString();
		if (value != null) {
			value = EscapeUtil.escapeHtml4(value);
		}
		return value;
	}

	@Override
	public String getParameter(String name) {
		String value = super.getParameter(name);
		if (value != null) {
			value = EscapeUtil.escapeHtml4(value);
		}
		return value;
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		if (values != null) {
			int length = values.length;
			String[] escapseValues = new String[length];
			for (int i = 0; i < length; i++) {
				escapseValues[i] = EscapeUtil.escapeHtml4(values[i]);
			}
			return escapseValues;
		}
		return values;
	}

}
