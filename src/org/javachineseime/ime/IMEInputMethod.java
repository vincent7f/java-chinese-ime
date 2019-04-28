package org.javachineseime.ime;

import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.awt.event.InputMethodEvent;
import java.awt.event.KeyEvent;
import java.awt.font.TextHitInfo;
import java.awt.im.spi.InputMethod;
import java.awt.im.spi.InputMethodContext;
import java.io.IOException;
import java.text.AttributedString;
import java.util.Locale;

import javax.swing.JFrame;

import org.javachineseime.ime.common.Utils;

/**
 * 支持jdk 1.4 以上的版本
 * 
 * @author luosheng 2006/9/25
 *         <ul>
 *         <li> msn:luosheng_lqdnnl@hotmail.com
 *         <li>
 *         </ul>
 * @author vincent7f
 * 
 */
public class IMEInputMethod implements InputMethod {
	private InputMethodContext	inputMethodContext	= null;
	private IMELookupPanel		lookup				= null;
	private StringBuffer		imein				= null;
	private IMEImp				ime					= null;
	private boolean				active				= false;
	private boolean				disposed			= false;
	static final String			statusWindowTitle	= "JAVA Chinese IME";
	static Window				statusWindow		= null;
	static IMEStatePanel		statePanel			= null;
	private Locale				locale				= Utils.getLocale() == null ? IMEDescriptor.DEFAULT_LOCALE //IMEDescriptor.SIMPLIFIED_CHINESE_PING_YI
															: Utils.getLocale();
	

	public IMEInputMethod() throws IOException {
		ime = new IMEImp(locale);
		imein = new StringBuffer();
	}

	public void setInputMethodContext(InputMethodContext context) {
		inputMethodContext = context;
		if (statusWindow == null) {
			Window sw = context.createInputMethodJFrame(statusWindowTitle,
					false);
			if (statePanel == null) {
				statePanel = new IMEStatePanel(this, (IMEImp) ime, locale,
						Utils.getSimpleToComplexFlag());
			}
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			sw.setBounds(
					IMEUIConfig.getInt("WINDOW_LEFT_OFFSET"),
					screenSize.height - IMEUIConfig.getInt("WINDOW_HEIGHT") - IMEUIConfig.getInt("WINDOW_BOTTOM_OFFSET"),
					IMEUIConfig.getInt("WINDOW_WIDTH"), 
					IMEUIConfig.getInt("WINDOW_HEIGHT"));
			
			
			synchronized (this.getClass()) {
				if (statusWindow == null) {
					statusWindow = sw;
					if (statusWindow instanceof JFrame) {
						((JFrame) statusWindow).getContentPane()
								.add(statePanel);
					}
				}
			}
		}
		inputMethodContext.enableClientWindowNotification(this, true);
	}

	public void activate() {
		if (active) {
			System.out.println("IMEInputMethod.activate called while active");
		}
		synchronized (statusWindow) {
			if (!statusWindow.isVisible()) {
				statusWindow.setVisible(true);
			}
		}
		active = true;
	}

	public boolean setLocale(Locale locale) {
		if (locale.equals(IMEDescriptor.SIMPLIFIED_CHINESE_PING_YI)
				|| locale.equals(Locale.SIMPLIFIED_CHINESE)
				|| locale.equals(Locale.TRADITIONAL_CHINESE)) {
			if (statusWindow != null) {
				this.locale = locale;
				updateStatusWindow(locale);
			}
			return true;
		}
		return false;
	}

	void updateStatusWindow(Locale locale) {
		synchronized (statusWindow) {
			statePanel.setIMEState(locale);
		}
	}

	private void closeLookupWindow() {
		if (lookup != null) {
			lookup.setVisible(false);
			lookup = null;
		}
	}

	public void deactivate(boolean isTemporary) {
		synchronized (statusWindow) {
			statusWindow.setVisible(false);
		}
		closeLookupWindow();
		if (!active) {
			System.out
					.println("IMEInputMethod.deactivate called while not active");
		}
		active = false;
	}

	public void dispatchEvent(AWTEvent event) {
		if (!active && (event instanceof KeyEvent)) {
		}
		if (disposed) {
		}
		if (!(event instanceof InputEvent)) {
		}
		//
		// state panel dispatchEvent
		//
		if (event.getID() == KeyEvent.KEY_RELEASED) {
			if (statePanel != null) {
				KeyEvent e = (KeyEvent) event;
				int keyCode = e.getKeyCode();
				if (active && e.isControlDown() == true
						&& keyCode == KeyEvent.VK_SHIFT) {
					statePanel.setIMEState();
				} else if (active
						&& e.isControlDown() == true
						&& (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_Z)) {
					statePanel.setSimpleToComplexState();
				}
				if (e.isControlDown() && keyCode == KeyEvent.VK_SPACE) {
					if (active) {
						deactivate(true);
					} else {
						activate();
					}
				}
			}
		}
		//
		// java input window
		//
		if (event.getID() == KeyEvent.KEY_RELEASED) {
			if (lookup != null) {
				KeyEvent e = (KeyEvent) event;
				switch(e.getKeyCode()) {
					case KeyEvent.VK_MINUS: // - with the _
					case KeyEvent.VK_LEFT:
						ime.prev();
						lookup.setlbSelectText(ime.out());
						break;					
					case KeyEvent.VK_EQUALS: // =
					case KeyEvent.VK_RIGHT: // arrow right
						ime.next();
						lookup.setlbSelectText(ime.out());
						break;		
					case KeyEvent.VK_DOWN:
						ime.last();
						lookup.setlbSelectText(ime.out());
						break;
					case KeyEvent.VK_UP:
						ime.first();
						lookup.setlbSelectText(ime.out());
						break;
					case KeyEvent.VK_ENTER:
					case KeyEvent.VK_ESCAPE:
						imein.setLength(0);
						lookup.setlbIMEInText("");
						closeLookupWindow();
						break;
				}
				
				if (active && imein.length() > 0) {
					e.consume();
				}
			}			
		} else if (active && event.getID() == KeyEvent.KEY_TYPED) {
			KeyEvent e = (KeyEvent) event;
			if (handleCharacter(e.getKeyChar())) {
				e.consume();
			} else {
				if (imein.length() > 0) {
					e.consume();
				}
			}
		} else if (active && event.getID() == KeyEvent.KEY_PRESSED) {
			KeyEvent e = (KeyEvent) event;
			if (imein.length() > 0) {
				e.consume();
			}
		}
	}

	/**
	 * Attempts to handle a typed character.
	 * 
	 * @return whether the character was handled
	 */
	private boolean handleCharacter(char c) {
		int imecount = ime.getCount();
		if (active) {
			if (c >= 'a' && c <= 'z') {
				openLookupWindow();
				imein.append(c);
				String s = imein.toString();
				lookup.setlbIMEInText(s);
				ime.find(s);
				lookup.setlbSelectText(ime.out());
				return true;
			} 
			if (lookup != null) {
				if (c == '\b') {
					int len = imein.length();
					if (len > 0) {
						imein.setLength(len - 1);
						String s = imein.toString();
						lookup.setlbIMEInText(s);
						ime.find(s);
						lookup.setlbSelectText(ime.out());
					}
					if (imein.length() <= 0) {
						closeLookupWindow();
						return true;
					}
				} else if (c >= '0' && c <= '9' || c == ' ') {
					if (imecount == 0) {
						imein.setLength(0);
						lookup.setlbIMEInText("");
						closeLookupWindow();
						return true;
					}
					int p = c - '0';
					if (p == 0) {
						p = 10;
					}
					p--;
					if (c == ' ') {
						p = 0;
					}
					{
						String s = ime.select(p);
						sendText(s);
					}
					{
						imein.setLength(0);
						lookup.setlbIMEInText("");
					}
					closeLookupWindow();
					return true;
				} 
			}
		}
		return false;
	}

	public void dispose() {
		if (active) {
			System.out.println("IMEInputMethod.dispose called while active");
		}
		if (disposed) {
			System.out.println("IMEInputMethod.disposed called repeatedly");
		}
		closeLookupWindow();
		disposed = true;
	}

	public void endComposition() {
		if (imein.length() != 0) {
			String s = imein.toString();
			sendText(s);
			imein.setLength(0);
		}
		closeLookupWindow();
	}

	public Object getControlObject() {
		return null;
	}

	public Locale getLocale() {
		return Utils.getLocale() == null ? IMEDescriptor.DEFAULT_LOCALE//IMEDescriptor.SIMPLIFIED_CHINESE_PING_YI
				: Utils.getLocale();
	}

	public void hideWindows() {
		if (active) {
			System.out
					.println("IMEInputMethod.hideWindows called while active");
		}
		closeLookupWindow();
	}

	public boolean isCompositionEnabled() {
		return true;
	}

	public void notifyClientWindowChange(Rectangle location) {
	}

	/** 显示窗口 */
	private void openLookupWindow() {
		Locale locale = Utils.getLocale() == null ? IMEDescriptor.DEFAULT_LOCALE //MEDescriptor.SIMPLIFIED_CHINESE_PING_YI
				: Utils.getLocale();
		if (lookup == null) {
			lookup = new IMELookupPanel(this, inputMethodContext, (IMEImp) ime,
					locale, Utils.getSimpleToComplexFlag());
		} else {
			lookup.setIMEState(locale);
			lookup.setSimpleToComplexState(Utils.getSimpleToComplexFlag());
		}
		lookup.setlbIMEInText("");
		lookup.setVisible(true);
	}

	public void reconvert() {
		throw new UnsupportedOperationException();
	}

	public void removeNotify() {
	}

	private void sendText(String s) {
		AttributedString as = new AttributedString(s);
		TextHitInfo caret = null;
		int count = s.length();
		// System.out.println("send " + s + "," + count);
		inputMethodContext.dispatchInputMethodEvent(
				InputMethodEvent.INPUT_METHOD_TEXT_CHANGED, as.getIterator(),
				count, caret, null);
	}

	public void setCharacterSubsets(Character.Subset[] subsets) {
	}

	public void setCompositionEnabled(boolean enable) {
		throw new UnsupportedOperationException();
	}

}
