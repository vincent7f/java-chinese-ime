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
public class CharTWLib implements IMELib {	
	private static Map<Integer, Map<String, List>> twMap = null;
	
    public CharTWLib() throws IOException {
        if (twMap == null) {   
        	twMap = new HashMap<Integer, Map<String, List>>();
            new Thread() {
                public void run() {
                    try {
                    	CharTWLib.init();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    public List find(String py) {
    	int lenght = py.length();    	
    	Map<String, List> keyMap = twMap.get(lenght);
    	if(keyMap == null){
    		return null;
    	}
    	return keyMap.get(py);    	
    }

    private static void init() throws IOException {
    	twMap = Utils.getKeyMap(IMEISN.CHINESE_CJ);
    }
}
