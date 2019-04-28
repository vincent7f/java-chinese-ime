package org.javachineseime.ime;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.javachineseime.ime.common.IME;
import org.javachineseime.ime.common.IMELib;
import org.javachineseime.ime.common.SimpleToComplexFlag;
import org.javachineseime.ime.py.CharPyLib;
import org.javachineseime.ime.py.WordPyLib;
import org.javachineseime.ime.tw.CharTWLib;
import org.javachineseime.ime.wb.CharWBLib;
import org.javachineseime.ime.wb.WordWBLib;

/**
 * @author luosheng 2006/9/25
 *         <ul>
 *         <li> msn:luosheng_lqdnnl@hotmail.com
 *         <li>
 *         </ul>
 * 
 */
public class IMEImp implements IME {

	private static boolean			inited				= false;
	private Locale					local				= null;
	private int						simpleToComplexFlag	= SimpleToComplexFlag.COMPLEX_TO_SIMPLE;
	private static IMELib			pyLibaryChar		= null;
	private static IMELib			pyLibaryWord		= null;

	private static IMELib			twLibaryChar		= null;
	private static IMELib			wbLibaryChar		= null;
	private static IMELib			wbLibaryWord		= null;
	private static SimpleToComplex	simpleToComplex		= null;

	private List					result				= new ArrayList();
	private int						start				= 0;

	public IMEImp(Locale local) {
		this.local = local;
		if (!inited) {
			inited = true;
			init();
		}
	}

	public void init() {
		try {
			twLibaryChar = new CharTWLib();
			wbLibaryChar = new CharWBLib();
			pyLibaryChar = new CharPyLib();
			pyLibaryWord = new WordPyLib();
			wbLibaryWord = new WordWBLib();
			simpleToComplex = new SimpleToComplex();
			System.out.println("init ok");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void find(String py) {
		result.clear();
		if (local.equals(IMEDescriptor.SIMPLIFIED_CHINESE_PING_YI)) {
			List charPyList = pyLibaryChar.find(py);
			List wordPyList = pyLibaryWord.find(py);
			if (charPyList != null) {
				result.addAll(charPyList);
			}
			if (wordPyList != null) {
				result.addAll(wordPyList);
			}
		} else if (local.equals(Locale.SIMPLIFIED_CHINESE)) {
			List charWBList = wbLibaryChar.find(py);
			if (charWBList != null) {
				result.addAll(charWBList);
			}
			List wordWBList = wbLibaryWord.find(py);
			if (wordWBList != null) {
				result.addAll(wordWBList);
			}
		} else if (local.equals(Locale.TRADITIONAL_CHINESE)) {
			List charTWList = twLibaryChar.find(py);
			if (charTWList != null) {
				result.addAll(charTWList);
			}
		} else {
			System.out.println("unknown locale: " + local);
		}
		start = 0;
	}

	public String out() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < MAX_SELECTION; i++) {
			String h = (i == 9 ? "0" : i + 1 + "");
			String s = getTxt(start + i);
			if (s == null) {
				break;
			}
			s = h + s + " ";
			sb.append(s);
		}
		return sb.toString();
	}

	public void prev() {
		if (start > 0) {
			start -= MAX_SELECTION;
		}
	}

	public void next() {
		if (start + MAX_SELECTION < result.size()) {
			start += MAX_SELECTION;
		}
	}

	public void first() {
		start = 0;
	}

	public void last() {
		int last = result.size() % MAX_SELECTION;
		start = result.size() - last;
	}

	private String getTxt(int i) {
		if (i >= 0 && i < result.size()) {
			return (String) result.get(i);
		} else {
			return null;
		}
	}

	public String select(int index) {
		String selectText = getTxt(start + index);

		if (simpleToComplexFlag == SimpleToComplexFlag.COMPLEX_TO_SIMPLE) {
			selectText = simpleToComplex.toSimple(selectText);
		} else if (simpleToComplexFlag == SimpleToComplexFlag.SIMPLE_TO_COMPLEX) {
			selectText = simpleToComplex.toComplex(selectText);
		}

		return selectText;
	}

	public int getCount() {
		return result.size();
	}

	public void setLocal(Locale local) {
		this.local = local;
	}

	public int getSimpleToComplexFlag() {
		return simpleToComplexFlag;
	}

	public void setSimpleToComplexFlag(int simpleToComplexFlag) {
		this.simpleToComplexFlag = simpleToComplexFlag;
	}

}
