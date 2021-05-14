/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shulie.tro.cloud.common.serialize;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 字符串格式序列化类
 *
 * @author shulie
 * @2018年5月21日
 * @version v1.0
 */
public class ToStringFormatSerialize extends JsonSerializer<String>{

	/**
	 * 字符串转序列化方法
	 * @author shulie
	 * @2018年5月21日
	 * @param value 字符串value
	 * @param gen JsonGenerator gen
	 * @param serializers JsonGenerator serializers
	 * @version v1.0
	 */
	@Override
	public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		gen.writeString(subZeroAndDot(value.toString()));

	}

	/**
	 * 字符串转换
	 * 这个不知道有啥用
	 * @author shulie
	 * @2018年5月21日
	 * @param s 字符串s
	 * @return 字符串s
	 * @version v1.0
	 */
  public String subZeroAndDot(String s){
	  return s;
  }

}
