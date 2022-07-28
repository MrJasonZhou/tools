/**  */
package com.jasonzhou.tool.sag.config;

import java.util.HashMap;
import java.util.Map;

import com.jasonzhou.tool.sag.info.VarDefine;

/**
 * 入力データのレイアウト定義
 * 
 * @author Jason Zhou
 *
 */
public class InputDataDefine {
	
	private Map<String, VarDefine> globalMap = new HashMap<>();
	
	private Map<String, VarDefine> loopMap = new HashMap<>();
	
	
	
	/** ループ開始行 */
	private int loopStartRowNo = 0;
	
	/**
	 * 変数定義を登録する
	 * 
	 * @param define	変数定義
	 */
	public void registe(VarDefine define) {
//		switch(define.getKbn()) {
//		case GLOBAL :
//			globalMap.put(define.getVarName(), define);
//			break;
//		case LOOP:
//			loopMap.put(define.getVarName(), define);
//			loopStartRowNo = define.getPosistion().getRow();
//			break;
//		default:
//			break;
//		}
	}

	/**
	 * 変数定義を取得する
	 * 
	 * @param name	変数名
	 * @return　変数定義
	 */
	public VarDefine get(String name) {
		VarDefine define = globalMap.get(name);
		if (define == null) {
			define = loopMap.get( name);
		}
		return define;
	}

	/**
	 * グローバル変数マップ を取得する
	
	 * @return グローバル変数マップ
	 */
	public Map<String, VarDefine> getGlobalMap() {
		return globalMap;
	}

	/**
	 * ロープ変数マップ を取得する
	
	 * @return ロープ変数マップ
	 */
	public Map<String, VarDefine> getLoopMap() {
		return loopMap;
	}

	/**
	 * ループ開始行 を取得する
	
	 * @return ループ開始行
	 */
	public int getLoopStartRowNo() {
		return loopStartRowNo;
	}

}
