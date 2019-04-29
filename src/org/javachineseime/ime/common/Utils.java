package org.javachineseime.ime.common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Utils {

	private static Locale	locale				= null;
	private static int		simpleToComplexFlag	= SimpleToComplexFlag.COMPLEX_TO_SIMPLE;

	/** ��ȡ key=value �������ļ������� */
	public static Map<Integer, Map<String, List>> getKeyMap(String fileName) {
		System.out.println("==================��ȡ" + fileName);
		Map<Integer, Map<String, List>> map = new HashMap<Integer, Map<String, List>>(
				50);
		try {
			long time = System.currentTimeMillis();
			List<String> strs = getLineStr(fileName);

			for (int i = 0; i < strs.size(); i++) {
				String line = strs.get(i);
				int equalSymbol = line.indexOf("=");
				if (equalSymbol <= -1) {
					continue;
				}
				//
				// substring (i,i) Ҫ�� split() ����Ҫ��
				//
				String key = line.substring(0, equalSymbol);
				String value = line.substring(equalSymbol + 1, line.length());

				int keyLength = key.length();

				String word = "";
				for (int j = 0; j < keyLength; j++) {
					//
					// key.charAt Ҫ�� key.toCharArray ����Ҫ��
					//
					word += String.valueOf(key.charAt(j));
					Map<String, List> keyMap = map.get(j + 1);
					if (keyMap == null) {
						keyMap = new HashMap<String, List>(1000);
						map.put(j + 1, keyMap);
					}
					List list = keyMap.get(word);
					if (list == null) {
						list = new ArrayList();
						keyMap.put(word, list);
					}
					list.add(value);
				}
			}
			System.out.println("=================="
					+ (System.currentTimeMillis() - time));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return map;
	}

	/** ��ȡpy.tab.txt */
	public static Map<Integer, Map<String, List>> getSinglePyMap(String fileName) {
		System.out.println("==================��ȡ" + fileName);
		Map<Integer, Map<String, List>> map = new HashMap<Integer, Map<String, List>>(
				50);
		try {
			long time = System.currentTimeMillis();
			List<String> strs = getLineStr(fileName);

			for (int i = 0; i < strs.size(); i++) {
				String line = strs.get(i);
				// System.out.println(line);
				int equalSymbol = line.indexOf("=");
				String key = line.substring(0, equalSymbol);
				String value = line.substring(equalSymbol + 1, line.length());

				int keyLength = key.length();

				String word = "";
				for (int j = 0; j < keyLength; j++) {
					word += String.valueOf(key.charAt(j));

					Map<String, List> keyMap = map.get(j + 1);
					if (keyMap == null) {
						keyMap = new HashMap<String, List>(1000);
						map.put(j + 1, keyMap);
					}
					List list = keyMap.get(word);
					if (list == null) {
						list = new ArrayList();
						keyMap.put(word, list);
					}
					int count = value.length();
					for (int k = 0; k < count; k++) {
						String temp = String.valueOf(value.charAt(k));
						list.add(temp);
						// System.out.println(word+"="+temp);
					}
				}
			}
			System.out.println("=================="
					+ (System.currentTimeMillis() - time));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return map;
	}

	public static List<GbkToBig5> getGbkToBig5(String fileName) {
		List<GbkToBig5> list = new ArrayList<GbkToBig5>();
		try {
			long time = System.currentTimeMillis();
			List<String> strs = getLineStr(fileName);
			for (String line : strs) {
				int equalSymbol = line.indexOf("=");
				if (equalSymbol <= -1) {
					continue;
				}
				String key = line.substring(0, equalSymbol);
				String value = line.substring(equalSymbol + 1, line.length());
				GbkToBig5 gbkToBig5 = new GbkToBig5();
				gbkToBig5.setSimpleName(key);
				gbkToBig5.setComplexName(value);
				list.add(gbkToBig5);
			}
			System.out.println("==================read " + fileName
					+ " time = " + (System.currentTimeMillis() - time));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}

	public static List<String> getLineStr(String fileName)
			throws UnsupportedEncodingException, IOException {
		ClassLoader ccl = Thread.currentThread().getContextClassLoader();
		BufferedReader br2 = null;
		List<String> strs = new ArrayList<String>(5000);
		try {
			try {//load from local folder
				br2 = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
			} catch (Exception e) {
				// load from class loader
				br2 = new BufferedReader(new InputStreamReader(ccl
						.getResourceAsStream(fileName), "UTF-8"));
			}
			String line = "";
			while ((line = br2.readLine()) != null) {
				strs.add(line);
			}
		} catch (UnsupportedEncodingException ex) {
			throw ex;
		} catch (IOException ex) {
			throw ex;
		} finally {
			try {
				if (br2 != null) {
					br2.close();
				}
			} catch (Exception ex) {
			}
		}
		return strs;
	}

	public static Locale getLocale() {
		return locale;
	}

	public static void setLocale(Locale locale) {
		Utils.locale = locale;
	}

	public static int getSimpleToComplexFlag() {
		return simpleToComplexFlag;
	}

	public static void setSimpleToComplexFlag(int simpleToComplexFlag) {
		Utils.simpleToComplexFlag = simpleToComplexFlag;
	}

}
