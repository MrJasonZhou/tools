/**  */
package com.jasonzhou.tool.sag.config;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Jason Zhou
 *
 */
public class MappingDefine {
	
	private String id;
	
	private Map<String, String> map = new HashMap<>();
	
	private static Map<String, MappingDefine> defineMap = new HashMap<>();
	
	public static MappingDefine getDefine(String id) {
		MappingDefine md = defineMap.get(id);
		if (md == null) {
			md = new MappingDefine(id);
			defineMap.put(id, md);
		}
		return md;
	}
	
	public MappingDefine(String id) {
		this.id = id;
	}
	
	public String get(String key) {
		return map.get(key);
	}
	
	public void set(String key, String value) {
		map.put(key, value);
	}

}
