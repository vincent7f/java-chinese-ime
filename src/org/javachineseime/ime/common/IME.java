package org.javachineseime.ime.common;

/**
 * @author luosheng 2006/9/25
 *         <ul>
 *         <li> msn:luosheng_lqdnnl@hotmail.com
 *         <li>
 *         </ul>
 * 
 */
public interface IME {
	/** max selection count */
	static final int	MAX_SELECTION	= 10;

	/** find match all list<String> */
	void find(String imeInCode);

	/** out match MAX_SELECTION count */
	String out();

	/** find match all list<String> prev */
	void prev();

	/** find match all list<String> next */
	void next();

	/** find match all list<String> first */
	void first();

	/** find match all list<String> last */
	void last();

	/** find match all list<String> size */
	int getCount();

	/** find match all list<String> in index return select words */
	String select(int index);
}
