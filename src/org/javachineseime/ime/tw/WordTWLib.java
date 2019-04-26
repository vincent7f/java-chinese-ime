package org.javachineseime.ime.tw;

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
public class WordTWLib implements IMELib {

	private static Map<Integer, Map<String, List>> twMap = null;


    public WordTWLib() throws IOException {
        if (twMap == null) {
        	twMap = new HashMap<Integer, Map<String, List>>();
            new Thread() {
                public void run() {
                    try {
                        WordTWLib.init();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    private static void init() throws IOException {
    	twMap = Utils.getKeyMap(IMEISN.CHINESE_CJ);
    }


    public List find(String py) {
    	int lenght = py.length();
    	Map<String, List> keyMap = twMap.get(lenght);
    	if(keyMap == null){
    		return null;
    	}
    	return keyMap.get(py); 
    }

}
