package com.yves.spring.spring.beans;

import lombok.Data;

@Data
public class PropertyValue {

	private String name;

	private Object value;

	public PropertyValue(String name, Object value) {
		super();
		this.name = name;
		this.value = value;
	}

}
