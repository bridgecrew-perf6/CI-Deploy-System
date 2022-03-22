package com.deploymentsys.tcp.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.deploymentsys.tcp.serialize.Serializer;
import com.deploymentsys.tcp.serialize.SerializerAlogrithm;

public class JSONSerializer implements Serializer {

	@Override
	public byte getSerializerAlogrithm() {
		return SerializerAlogrithm.JSON;
	}

	@Override
	public byte[] serialize(Object object) {

		return JSON.toJSONBytes(object);
	}

	@Override
	public <T> T deserialize(Class<T> clazz, byte[] bytes) {

		return JSON.parseObject(bytes, clazz);
	}
}
