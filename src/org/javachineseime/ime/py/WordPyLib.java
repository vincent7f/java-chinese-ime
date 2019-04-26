package org.javachineseime.ime.py;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javachineseime.ime.common.IMEISN;
import org.javachineseime.ime.common.IMELib;
import org.javachineseime.ime.common.Utils;
/**
 * @author luosheng 2006/9/25
 *         <ul>
 *         <li> msn:luosheng_lqdnnl@hotmail.com
 *         <li>
 *         </ul>
 * 
 */
public class WordPyLib implements IMELib {

	private static Map<Integer, Map<String, List>> pyMap = null;


    public WordPyLib() throws IOException {
        if (pyMap == null) {
        	pyMap = new HashMap<Integer, Map<String, List>>();
            new Thread() {
                public void run() {
                    try {
                        WordPyLib.init();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    private static void init() throws IOException {
    	pyMap = Utils.getKeyMap(IMEISN.CHINESE_PY_WORDS);
    }


    public List find(String py) {
    	int lenght = py.length();
    	Map<String, List> keyMap = pyMap.get(lenght);
    	if(keyMap == null){
    		return null;
    	}
    	return keyMap.get(py); 
    }

}
