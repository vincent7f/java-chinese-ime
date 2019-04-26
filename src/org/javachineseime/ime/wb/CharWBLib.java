package org.javachineseime.ime.wb;

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
public class CharWBLib implements IMELib {	
	private static Map<Integer, Map<String, List>> wbMap = null;
	
    public CharWBLib() throws IOException {
        if (wbMap == null) {   
        	wbMap = new HashMap<Integer, Map<String, List>>();
            new Thread() {
                public void run() {
                    try {
                    	CharWBLib.init();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    public List find(String py) {
    	int lenght = py.length();    	
    	Map<String, List> keyMap = wbMap.get(lenght);
    	if(keyMap == null){
    		return null;
    	}
    	return keyMap.get(py);    	
    }

    private static void init() throws IOException {
    	wbMap = Utils.getKeyMap(IMEISN.CHINESE_WB86);
    }
}
