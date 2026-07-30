package com.prj.springai.piotrblog.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Gender {
	MALE, FEMALE;
	
	@JsonCreator
    public static Gender from(String value) {
		return value.toUpperCase().startsWith("MALE") ? Gender.MALE : Gender.FEMALE;
//        return Gender.valueOf(value.toUpperCase());
    }
}
