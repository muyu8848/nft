package com.nft.config.xss;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import cn.hutool.core.util.EscapeUtil;

public class XssStringJsonDeserializer extends JsonDeserializer<String> {

	@Override
	public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String value = p.getValueAsString();
		if (value != null) {
			return EscapeUtil.escapeHtml4(value.toString());
		}
		return value;
	}

}
