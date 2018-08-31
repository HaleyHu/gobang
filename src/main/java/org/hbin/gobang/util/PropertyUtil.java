package org.hbin.gobang.util;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertyUtil {
	private static final String fileName = "config.properties";
	private static final Properties prop = new Properties();
	private static final Logger log = Logger.getLogger(PropertyUtil.class);
	
	static {
		try {
			log.info("loading properties ...");
			prop.load(PropertyUtil.class.getClassLoader().getResourceAsStream(fileName));
		} catch (IOException e) {
			log.error("read properties error", e);
			System.exit(0);
		}
	}
	
	public static String getString(String key) {
		String value = prop.getProperty(key);
		log.info("[PropertyUtil getString(String)] " + key + "=" + value);
		return value;
	}
	
	public static String getString(String key, String defaultValue) {
		String value = prop.getProperty(key, defaultValue);
		log.info("[PropertyUtil getString(String, String)] " + key + "=" + value);
		return value;
	}
	
	public static int getInt(String key) {
		int value = StringUtil.toInt(prop.getProperty(key), 0);
		log.info("[PropertyUtil getInt(String)] " + key + "=" + value);
		return value;
	}
	
	public static int getInt(String key, int defaultValue) {
		int value = StringUtil.toInt(prop.getProperty(key), defaultValue);
		log.info("[PropertyUtil getInt(String, int)] " + key + "=" + value);
		return value;
	}
	
	public static long getLong(String key) {
		long value = StringUtil.toLong(prop.getProperty(key), 0);
		log.info("[PropertyUtil getLong(String)] " + key + "=" + value);
		return value;
	}
	
	public static long getLong(String key, long defaultValue) {
		long value = StringUtil.toLong(prop.getProperty(key), defaultValue);
		log.info("[PropertyUtil getLong(String, long)] " + key + "=" + value);
		return value;
	}
	
	public static double getDouble(String key) {
		double value = StringUtil.toDouble(prop.getProperty(key), 0);
		log.info("[PropertyUtil getDouble(String)] " + key + "=" + value);
		return value;
	}
	
	public static double getDouble(String key, double defaultValue) {
		double value = StringUtil.toDouble(prop.getProperty(key), defaultValue);
		log.info("[PropertyUtil getDouble(String, double)] " + key + "=" + value);
		return value;
	}
}