package org.javachineseime.ime;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.font.TextHitInfo;
import java.awt.im.spi.InputMethodContext;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.javachineseime.ime.common.SimpleToComplexFlag;
import org.javachineseime.ime.common.Utils;
import javax.swing.SwingConstants;

/**
 * @author luosheng 2006/9/25
 *         <ul>
 *         <li> msn:luosheng_lqdnnl@hotmail.com
 *         <li>
 *         </ul>
 * 
 */
public class IMELookupPanel extends JPanel {

	private static final long	serialVersionUID	= -1;

	private IMEInputMethod		inputMethod			= null;
	private JLabel				lbSelectText		= null;
	private JLabel				lbShowIMEName		= null;
	private JPanel				jPanel				= null;
	private JLabel				lbIMEIn				= null;
	private JButton				btnFirst			= null;
	private JPanel				jPanel1				= null;
	private JButton				btnPre				= null;
	private Window				lookupWindow		= null;
	private JButton				btnNext				= null;
	private JButton				btnLast				= null;
	private IMEImp				ime					= null;
	private JPanel				jPanel2				= null;
	private JLabel				lbGbkToBig5			= null;
	private InputMethodContext	inputMethodContext	= null;
	private String				lookupWindowTitle	= "ÊäÈë´°¿Ú";
	private Locale				locale				= IMEDescriptor.SIMPLIFIED_CHINESE_PING_YI;

	public IMELookupPanel() {
		initialize();
	}

	public IMELookupPanel(IMEInputMethod inputMethod,
			InputMethodContext context, IMEImp ime, Locale locale,
			int simpleToComplexFlag) {
		initialize();
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		this.inputMethod = inputMethod;
		this.inputMethodContext = context;
		this.ime = ime;
		enableEvents(AWTEvent.KEY_EVENT_MASK);
		setIMEState(locale);
		setSimpleToComplexState(simpleToComplexFlag);
		lookupWindow = context.createInputMethodJFrame(lookupWindowTitle, true);
		setOpaque(true);
		setForeground(Color.black);
//		setBackground(Color.white);

		if (lookupWindow instanceof JFrame) {
			((JFrame) lookupWindow).getContentPane().add(this);
		} else {
			lookupWindow.add(this);
		}
		lookupWindow.setSize(IMEUIConfig.getInt("CANDIDATE_WINDOW_WIDTH"), IMEUIConfig.getInt("CANDIDATE_WINDOW_HEIGHT"));
		updateWindowLocation();
		lookupWindow.setVisible(true);
	}

	private void updateWindowLocation() {
		Point windowLocation = new Point();
		Rectangle caretRect = inputMethodContext.getTextLocation(TextHitInfo
				.leading(inputMethodContext.getInsertPositionOffset()));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension windowSize = lookupWindow.getSize();
		final int SPACING = 2;

		if (caretRect.x + windowSize.width > screenSize.width) {
			windowLocation.x = screenSize.width - windowSize.width;
		} else {
			windowLocation.x = caretRect.x;
		}

		if (caretRect.y + caretRect.height + SPACING + windowSize.height > screenSize.height) {
			windowLocation.y = caretRect.y - SPACING - windowSize.height;
		} else {
			windowLocation.y = caretRect.y + caretRect.height + SPACING;
		}

		lookupWindow.setLocation(windowLocation);
	}

	public void setVisible(boolean visible) {
		if (!visible) {
			lookupWindow.setVisible(false);
			lookupWindow.dispose();
			lookupWindow = null;
		}
		super.setVisible(visible);
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		lbShowIMEName = new JLabel();
		lbShowIMEName.setText("Æ´Òô");
		lbShowIMEName.setPreferredSize(new Dimension(32, 25));
		lbShowIMEName.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
		lbShowIMEName.setFont(new Font("Dialog", Font.BOLD, 14));
		lbSelectText = new JLabel();
		lbSelectText.setText("ÎÒÃÇ");
		lbSelectText.setVerticalTextPosition(SwingConstants.CENTER);
		lbSelectText.setVerticalAlignment(SwingConstants.CENTER);
		lbSelectText.setPreferredSize(new Dimension(26, 20));
		lbSelectText.setFont(new Font("Dialog", Font.PLAIN, 14));
		this.setLayout(new BorderLayout());
		this.setSize(new Dimension(454, 152));

		this.add(lbSelectText, BorderLayout.CENTER);
//		lbShowIMEName.addMouseListener(new java.awt.event.MouseAdapter() {
//			public void mouseClicked(java.awt.event.MouseEvent e) {
//				setIMEState();
//			}
//		});
		this.add(getJPanel(), BorderLayout.NORTH);
		this.add(getJPanel2(), BorderLayout.EAST);
	}

	/**
	 * This method initializes jPanel2
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel2() {
		if (jPanel2 == null) {
			FlowLayout flowLayout1 = new FlowLayout();
			flowLayout1.setVgap(0);
			flowLayout1.setHgap(2);
			lbGbkToBig5 = new JLabel();
//			lbGbkToBig5.addMouseListener(new java.awt.event.MouseAdapter() {
//				public void mouseClicked(java.awt.event.MouseEvent e) {
//					setSimpleToComplexState();
//				}
//			});
			lbGbkToBig5.setText("·±×ª¼ò");
			lbGbkToBig5.setFont(new Font("Dialog", Font.PLAIN, 14));
			lbGbkToBig5.setPreferredSize(new Dimension(45, 25));
			jPanel2 = new JPanel();
			jPanel2.setPreferredSize(new Dimension(85, 50));
			jPanel2.setLayout(flowLayout1);
			jPanel2.add(lbShowIMEName, null);
			jPanel2.add(lbGbkToBig5, null);
		}
		return jPanel2;
	}

	protected void processKeyEvent(KeyEvent event) {
		inputMethod.dispatchEvent(event);
	}

	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(new BorderLayout());
			jPanel.add(getLbIMEIn(), BorderLayout.CENTER);
			jPanel.add(getJPanel1(), BorderLayout.EAST);
		}
		return jPanel;
	}

	/**
	 * This method initializes lbIMEIn
	 * 
	 * @return javax.swing.JLabel
	 */
	private JLabel getLbIMEIn() {
		if (lbIMEIn == null) {
			lbIMEIn = new JLabel();
			lbIMEIn.setText("abc");
			lbIMEIn.setFont(new Font("Dialog", Font.PLAIN, 14));
		}
		return lbIMEIn;
	}

	/**
	 * This method initializes jPanel1
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			FlowLayout flowLayout = new FlowLayout();
			flowLayout.setVgap(2);
			flowLayout.setHgap(2);
			jPanel1 = new JPanel();
			jPanel1.setLayout(flowLayout);
			jPanel1.add(getBtnFirst(), null);
			jPanel1.add(getBtnPre(), null);
			jPanel1.add(getBtnNext(), null);
			jPanel1.add(getBtnLast(), null);
		}
		return jPanel1;
	}

	/**
	 * This method initializes btnFirst
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnFirst() {
		if (btnFirst == null) {
			btnFirst = new JButton();
			btnFirst.setText("");
			btnFirst.setIcon(new ImageIcon(getClass().getResource(
					"/org/javachineseime/ime/icon/first.gif")));
			btnFirst.setPreferredSize(new Dimension(18, 16));
			btnFirst.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ime.first();
					setlbSelectText(ime.out());
				}
			});
		}
		return btnFirst;
	}

	/**
	 * This method initializes btnPre
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnPre() {
		if (btnPre == null) {
			btnPre = new JButton();
			btnPre.setIcon(new ImageIcon(getClass().getResource(
					"/org/javachineseime/ime/icon/pre.gif")));
			btnPre.setPreferredSize(new Dimension(18, 16));
			btnPre.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ime.prev();
					setlbSelectText(ime.out());
				}
			});
		}
		return btnPre;
	}

	/**
	 * This method initializes btnBack
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnNext() {
		if (btnNext == null) {
			btnNext = new JButton();
			btnNext.setIcon(new ImageIcon(getClass().getResource(
					"/org/javachineseime/ime/icon/back.gif")));
			btnNext.setPreferredSize(new Dimension(18, 16));
			btnNext.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ime.next();
					setlbSelectText(ime.out());
				}
			});
		}
		return btnNext;
	}

	/**
	 * This method initializes btnLast
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnLast() {
		if (btnLast == null) {
			btnLast = new JButton();
			btnLast.setIcon(new ImageIcon(getClass().getResource(
					"/org/javachineseime/ime/icon/last.gif")));
			btnLast.setPreferredSize(new Dimension(18, 16));
			btnLast.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ime.last();
					setlbSelectText(ime.out());
				}
			});
		}
		return btnLast;
	}

	public void setIMEState() {
		if (locale.equals(IMEDescriptor.SIMPLIFIED_CHINESE_PING_YI)) {
			locale = Locale.SIMPLIFIED_CHINESE;
		} else if (locale.equals(Locale.SIMPLIFIED_CHINESE)) {
			locale = Locale.TRADITIONAL_CHINESE;
		} else if (locale.equals(Locale.TRADITIONAL_CHINESE)) {
			locale = IMEDescriptor.SIMPLIFIED_CHINESE_PING_YI;
		}
		setIMEState(locale);
	}

	public void setIMEState(Locale locale) {
		this.locale = locale;
		ime.setLocal(locale);
		Utils.setLocale(locale);
		if (this.locale.equals(IMEDescriptor.SIMPLIFIED_CHINESE_PING_YI)) {
			this.lbShowIMEName.setText("Æ´Òô");
		} else if (this.locale.equals(Locale.SIMPLIFIED_CHINESE)) {
			this.lbShowIMEName.setText("Îå±Ê");
		} else if (this.locale.equals(Locale.TRADITIONAL_CHINESE)) {
			this.lbShowIMEName.setText("²Öò¡");
		}
	}

	public void setSimpleToComplexState() {
		int simpleToComplexFlag = Utils.getSimpleToComplexFlag();
		if (simpleToComplexFlag == SimpleToComplexFlag.COMPLEX_TO_SIMPLE) {
			simpleToComplexFlag = SimpleToComplexFlag.SIMPLE_TO_COMPLEX;
		} else if (simpleToComplexFlag == SimpleToComplexFlag.SIMPLE_TO_COMPLEX) {
			simpleToComplexFlag = SimpleToComplexFlag.NO;
		} else if (simpleToComplexFlag == SimpleToComplexFlag.NO) {
			simpleToComplexFlag = SimpleToComplexFlag.COMPLEX_TO_SIMPLE;
		}
		setSimpleToComplexState(simpleToComplexFlag);
	}

	public void setSimpleToComplexState(int simpleToComplexFlag) {
		ime.setSimpleToComplexFlag(simpleToComplexFlag);
		Utils.setSimpleToComplexFlag(simpleToComplexFlag);
		if (simpleToComplexFlag == SimpleToComplexFlag.COMPLEX_TO_SIMPLE) {
			lbGbkToBig5.setText("·±×ª¼ò");
		} else if (simpleToComplexFlag == SimpleToComplexFlag.SIMPLE_TO_COMPLEX) {
			lbGbkToBig5.setText("¼ò×ª·±");
		} else if (simpleToComplexFlag == SimpleToComplexFlag.NO) {
			lbGbkToBig5.setText("²»×ª»»");
		}
	}

	public Locale getLocalee() {
		return locale;
	}

	public void setLocalee(Locale locale) {
		this.locale = locale;
	}

	public void setlbIMEInText(String text) {
		this.lbIMEIn.setText(text);
	}

	public void setlbSelectText(String text) {
		this.lbSelectText.setText(text);
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
