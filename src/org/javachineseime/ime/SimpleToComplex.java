/*
 * Created on 2006-2-23
 *
 * 
 */
package org.javachineseime.ime;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javachineseime.ime.common.GbkToBig5;
import org.javachineseime.ime.common.IMEISN;
import org.javachineseime.ime.common.Utils;

/**
 * @author luosheng 2006/9/25
 *         <ul>
 *         <li> msn:luosheng_lqdnnl@hotmail.com
 *         <li>
 *         </ul>
 * 
 */
public class SimpleToComplex {

	private static Map<String, String>	big5KeyMap	= null;
	private static Map<String, String>	gbkKeyMap	= new HashMap<String, String>();

	public SimpleToComplex() throws IOException {
		if (big5KeyMap == null) {
			big5KeyMap = new HashMap<String, String>();
			new Thread() {
				public void run() {
					try {
						SimpleToComplex.init();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}.start();
		}
	}

	private static void init() throws IOException {
		List<GbkToBig5> list = Utils.getGbkToBig5(IMEISN.SIMPLE_COMPLEX);
		for (GbkToBig5 g : list) {
			gbkKeyMap.put(g.getSimpleName(), g.getComplexName());
			big5KeyMap.put(g.getComplexName(), g.getSimpleName());
		}
	}

	/** to chinese simple */
	public String toSimple(String s) {
		String reutrnStr = "";
		char[] chars = s.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			String word = String.valueOf(chars[i]);
			if (word.getBytes().length == 2) {
				String gbkWord = big5KeyMap.get(word);
				if (gbkWord != null) {
					word = gbkWord;
				}
			}
			reutrnStr += word;
		}
		return reutrnStr;
	}

	/** to chinese Complex */
	public String toComplex(String s) {
		String reutrnStr = "";
		char[] chars = s.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			String word = String.valueOf(chars[i]);
			if (word.getBytes().length == 2) {
				String big5Word = gbkKeyMap.get(word);
				if (big5Word != null) {
					word = big5Word;
				}
			}
			reutrnStr += word;
		}
		return reutrnStr;
	}

}
