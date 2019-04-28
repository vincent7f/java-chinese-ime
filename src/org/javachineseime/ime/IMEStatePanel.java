package org.javachineseime.ime;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.javachineseime.ime.common.SimpleToComplexFlag;
import org.javachineseime.ime.common.Utils;

/**
 * @author luosheng 2006/9/25
 *         <ul>
 *         <li> msn:luosheng_lqdnnl@hotmail.com
 *         <li>
 *         </ul>
 * 
 */
public class IMEStatePanel extends JPanel {

	private static final long	serialVersionUID	= -6783322905930372890L;

	private IMEInputMethod		inputMethod			= null;
	private JLabel				lbShowIMEName		= null;
	private IMEImp				ime					= null;
	private JPanel				jPanel2				= null;
	private JLabel				lbGbkToBig5			= null;
	private Locale				locale				= IMEDescriptor.DEFAULT_LOCALE; //IMEDescriptor.SIMPLIFIED_CHINESE_PING_YI;
	

	public IMEStatePanel() {
		initialize();
	}

	public IMEStatePanel(IMEInputMethod inputMethod, IMEImp ime,Locale locale,int simpleToComplexFlag) {
		initialize();
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		this.inputMethod = inputMethod;
		this.ime = ime;
		enableEvents(AWTEvent.KEY_EVENT_MASK);
		enableEvents(AWTEvent.MOUSE_EVENT_MASK);
		setIMEState(locale);
		setSimpleToComplexState(simpleToComplexFlag);
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		lbShowIMEName = new JLabel();
		lbShowIMEName.setText("Æ´Òô(CTRL+SHIFT)");
//		lbShowIMEName.setForeground(new java.awt.Color(255, 153, 0));
		lbShowIMEName.setPreferredSize(new Dimension(115, 25));
		lbShowIMEName.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
		lbShowIMEName.setFont(new Font("Dialog", Font.BOLD, 12));
		this.setLayout(new BorderLayout());
		this.setSize(new Dimension(235, 29));

		this.add(getJPanel2(), BorderLayout.CENTER);
		lbShowIMEName.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				setIMEState();
			}
		});
	}

	
	/**
	 * This method initializes jPanel2
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			FlowLayout flowLayout1 = new FlowLayout();
			flowLayout1.setVgap(2);
			lbGbkToBig5 = new JLabel();
//			lbGbkToBig5.setForeground(new java.awt.Color(255, 153, 0));
			lbGbkToBig5.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent e) {
					setSimpleToComplexState();
				}
			});
			lbGbkToBig5.setText("·±×ª¼ò(CTRL+Z)");
			lbGbkToBig5.setPreferredSize(new Dimension(105, 25));
			jPanel2 = new JPanel();
			jPanel2.setPreferredSize(new Dimension(200, 29));
			jPanel2.setLayout(flowLayout1);
			jPanel2.add(lbShowIMEName, null);
			jPanel2.add(lbGbkToBig5, null);
		}
		return jPanel2;
	}
	
	
	protected void processKeyEvent(KeyEvent event) {
		inputMethod.dispatchEvent(event);
	}

	public void setIMEState() {
		if (locale.equals(IMEDescriptor.SIMPLIFIED_CHINESE_PING_YI)) {
			locale = Locale.SIMPLIFIED_CHINESE;
		} else if (locale.equals(Locale.SIMPLIFIED_CHINESE)) {
			locale = Locale.TRADITIONAL_CHINESE;
		} else if (locale.equals(Locale.TRADITIONAL_CHINESE)) {
			locale = IMEDescriptor.SIMPLIFIED_CHINESE_PING_YI;
		} else {
			System.out.println("unknown locale in setIMEState(): " + locale);
		}
		setIMEState(locale);
	}

	public void setIMEState(Locale locale) {
		this.locale = locale;
		ime.setLocal(locale);
		Utils.setLocale(locale);
		if (this.locale.equals(IMEDescriptor.SIMPLIFIED_CHINESE_PING_YI)) {
			this.lbShowIMEName.setText("Æ´Òô(CTRL+SHIFT)");
		} else if (this.locale.equals(Locale.SIMPLIFIED_CHINESE)) {
			this.lbShowIMEName.setText("Îå±Ê(CTRL+SHIFT)");
		} else if (this.locale.equals(Locale.TRADITIONAL_CHINESE)) {
			this.lbShowIMEName.setText("²Öò¡(CTRL+SHIFT)");
		} else {
			System.out.println("unknown locale in setIMEState(): " + locale);
		}
	}

	public void setSimpleToComplexState() {
		int simpleToComplexFlag = Utils.getSimpleToComplexFlag();
		if (simpleToComplexFlag == SimpleToComplexFlag.COMPLEX_TO_SIMPLE) {
			simpleToComplexFlag =SimpleToComplexFlag.SIMPLE_TO_COMPLEX;
		} else if (simpleToComplexFlag == SimpleToComplexFlag.SIMPLE_TO_COMPLEX) {
			simpleToComplexFlag =SimpleToComplexFlag.NO;
		} else if (simpleToComplexFlag == SimpleToComplexFlag.NO) {
			simpleToComplexFlag =SimpleToComplexFlag.COMPLEX_TO_SIMPLE;
		}
		setSimpleToComplexState(simpleToComplexFlag);
	}
	
	public void setSimpleToComplexState(int simpleToComplexFlag) {
		ime.setSimpleToComplexFlag(simpleToComplexFlag);
		Utils.setSimpleToComplexFlag(simpleToComplexFlag);
		if (simpleToComplexFlag == SimpleToComplexFlag.COMPLEX_TO_SIMPLE) {
			lbGbkToBig5.setText("·±×ª¼ò(CTRL+Z)");
		} else if (simpleToComplexFlag == SimpleToComplexFlag.SIMPLE_TO_COMPLEX) {
			lbGbkToBig5.setText("¼ò×ª·±(CTRL+Z)");
		} else if (simpleToComplexFlag == SimpleToComplexFlag.NO) {
			lbGbkToBig5.setText("²»×ª»»(CTRL+Z)");
		}
	}
	

	public Locale getLocalee() {
		return locale;
	}

	public void setLocalee(Locale locale) {
		this.locale = locale;
	}


}
