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

	public static final Locale SIMPLIFIED_CHINESE_PING_YI = new Locale("zh", "CN", "ƴ��");

	public Locale[] getAvailableLocales() throws AWTException {
		return new Locale[] { SIMPLIFIED_CHINESE_PING_YI, Locale.SIMPLIFIED_CHINESE, Locale.TRADITIONAL_CHINESE };
	}

	public boolean hasDynamicLocaleList() {
		return false;
	}

	public String getInputMethodDisplayName(Locale inputLocale, Locale displayLanguage) {
		if (displayLanguage == Locale.CHINESE //
				|| displayLanguage == Locale.CHINA //
				|| displayLanguage == Locale.SIMPLIFIED_CHINESE) {
			return "���뷨";
		} else if (displayLanguage == Locale.TRADITIONAL_CHINESE) {
			return "ݔ�뷨";
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
