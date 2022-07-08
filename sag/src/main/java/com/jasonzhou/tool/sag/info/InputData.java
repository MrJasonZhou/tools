/**  */
package com.jasonzhou.tool.sag.info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 入力データ
 * 
 * @author Jason Zhou
 *
 */
public class InputData {
	
	/** グローバル変数 */
	private Map<String, Variable> globalMap = new HashMap<>();
	
	/** LOOP変数のリスト */
	private List<Map<String, Variable>> loopList = new ArrayList<>();
	
	private String sheetName;
	
	private Map<String, Variable> lastRecord = new HashMap<>();
	/**
	 * グローバル変数を登録する
	 * 
	 * @param variable	変数	
	 */
	public void addGlobal(Variable variable) {
		if (variable.getDefine().getKbn() == VarKbn.GLOBAL) {
			globalMap.put(variable.getDefine().getVarName(), variable);
		}
	}
	
	/**
	 * ループレコードを登録する
	 * 
	 * @param record	レコード変数	
	 */
	public void addLoop(Map<String, Variable> record) {
		loopList.add(record);
	}

	/**
	 * グローバル変数を取得する
	 * 
	 * @param name	変数名
	 * @return	グローバル変数
	 */
	public Variable getGlobal(String name) {
		return globalMap.get(name);
	}

	/**
	 * LOOP変数のリスト を取得する
	
	 * @return LOOP変数のリスト
	 */
	public List<Map<String, Variable>> getLoopList() {
		return loopList;
	}
	
	/**
	 * 定義シート名 を取得する
	
	 * @return 定義シート名
	 */
	public String getSheetName() {
		return sheetName;
	}

	/**
	 * 定義シート名 を設定する
	 *
	 * @param sheetName 定義シート名
	 */
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	/**
	 * _lastRecord を取得する
	
	 * @return _lastRecord
	 */
	public Map<String, Variable> getLastRecord() {
		return lastRecord;
	}

	/**
	 * _lastRecord を設定する
	 *
	 * @param lastRecord _lastRecord
	 */
	public void setLastRecord(Map<String, Variable> lastRecord) {
		this.lastRecord = lastRecord;
	}
	
	/**
	 * データをマップに変換する
	 * 
	 * @return	マップ
	 */
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<>();
		for (Map.Entry<String, Variable> entry : globalMap.entrySet()) {
			String varName = entry.getKey();
			Variable var = entry.getValue();
			map.put(varName, var.getValue());
		}
		List<Map<String, Object>> rows = new ArrayList<>();
		for (Map<String, Variable> loop : loopList) {
			Map<String, Object> row = new HashMap<>();
			for (Map.Entry<String, Variable> entry : loop.entrySet()) {
				row.put(entry.getKey(), entry.getValue().getValue());
			}
			rows.add(row);
		}
		map.put("rows", rows);
		return map;
	}
}
