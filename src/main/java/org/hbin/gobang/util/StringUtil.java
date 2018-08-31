package org.hbin.gobang.util;

public class StringUtil {
	public static int toInt(String s) {
		return Integer.parseInt(s);
	}
	
	public static int toInt(String s, int defaultValue) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	public static long toLong(String s) {
		return Long.parseLong(s);
	}
	
	public static long toLong(String s, long defaultValue) {
		try {
			return Long.parseLong(s);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
	
	public static double toDouble(String s) {
		return Double.parseDouble(s);
	}
	
	public static double toDouble(String s, double defaultValue) {
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
}