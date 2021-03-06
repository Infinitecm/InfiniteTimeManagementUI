package com.infinite.tm.util;

import java.util.Base64;

import org.apache.commons.lang3.RandomStringUtils;

public class GeneratePassword {

	public static String newPassword()
	{
		final String chars ="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890!@#$%^&*()";
		return new String(Base64.getEncoder().encodeToString(RandomStringUtils.random(8, chars).getBytes()));
		
	}
}
