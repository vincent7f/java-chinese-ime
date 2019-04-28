package org.javachineseime.ime;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class IMEUIConfig {
	private static boolean isLoaded = false;
	private static Properties properties = new Properties();

	public static int getInt(String name) {
		init();
		
		String value = properties.getProperty(name);
		value = value.trim();
		System.out.println(name + " = " + value);
		return Integer.parseInt(value);
	}

	private static void init() {
		if (isLoaded) {
			return;
		}
		
		String filepath = "uiconfig.properties";
		try {
			InputStream inputStream = new FileInputStream(filepath);
			IMEUIConfig.properties.load(inputStream);			
		} catch (IOException e) {
			System.out.println("fail to load file: " + filepath);
			e.printStackTrace();
		}
		
		isLoaded = true;
	}

	public static void main(String[] args) {
		IMEUIConfig.init();
		System.out.println(IMEUIConfig.getInt("WINDOW_LEFT_OFFSET"));
	}
}
