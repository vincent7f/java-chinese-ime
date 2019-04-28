package org.javachineseime.ime;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.im.spi.InputMethod;
import java.awt.im.spi.InputMethodDescriptor;
import java.util.Locale;

/**
 * @author luosheng 2006/9/25
 *         <ul>
 *         <li>msn:luosheng_lqdnnl@hotmail.com
 *         <li>
 *         </ul>
 * 
 */
public class IMEDescriptor implements InputMethodDescriptor {

	public static final Locale SIMPLIFIED_CHINESE_PING_YI = new Locale("zh", "CN", "拼音");
	public static final Locale DEFAULT_LOCALE = Locale.SIMPLIFIED_CHINESE; // Default locale for IME

	public Locale[] getAvailableLocales() throws AWTException {
		return new Locale[] { Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE, SIMPLIFIED_CHINESE_PING_YI};
	}

	public boolean hasDynamicLocaleList() {
		return false;
	}

	public String getInputMethodDisplayName(Locale inputLocale, Locale displayLanguage) {
		if (displayLanguage == Locale.CHINESE //
				|| displayLanguage == Locale.CHINA //
				|| displayLanguage == Locale.SIMPLIFIED_CHINESE) {
			return "输入法";
		} else if (displayLanguage == Locale.TRADITIONAL_CHINESE) {
			return "輸入法";
		} else {
			return "Chinese IME";
		}

	}

	public Image getInputMethodIcon(Locale inputLocale) {
		return null;
	}

	public InputMethod createInputMethod() throws Exception {
		return new IMEInputMethod();
	}

}
