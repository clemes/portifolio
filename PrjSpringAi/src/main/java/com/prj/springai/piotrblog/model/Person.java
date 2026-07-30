package com.prj.springai.piotrblog.model;

public record Person(
		Integer id,
		String firstName,
		String lastName,
		Integer age,
		Gender gender,
		String nationality
		)
{}
