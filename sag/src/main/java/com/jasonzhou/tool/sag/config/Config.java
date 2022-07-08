/**  */
package com.jasonzhou.tool.sag.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 設定情報
 * 
 * @author Jason Zhou
 *
 */
abstract public class Config {
	
	/** 処理区分 */
	public static final String GLOBAL_KBN = "kbn";
	
	/** コメント接頭語 */
	public static final String COMMENT_SUFFIX = "commentSuffix";
	
	/** テンプレートフォルダ */
	public static final String TEMPLATE_DIR = "templateDir";
	
	/**
	 * 設定情報を読み込む
	 * 
	 * @param configFileName	設定ファイル名
	 * @throws IOException
	 */
	public abstract void load(String configFileName) throws IOException; 

	private  List<Task> taskList = new ArrayList<>();
	
	private  Map<String, String> varMap = new HashMap<>();
	
	/** テンプレート定義リスト */
	private List<TemplateDefine> templateDefineList = new ArrayList<>();
	
	/** 入力データ定義 */
	protected InputDataDefine inputDataDefine = new InputDataDefine();
	
	/** マッピング定義 */
	private MappingDefine mappingDefine = null;
	
	public Config() {
		
	}
	
	/**
	 * 入力データ定義 を取得する
	
	 * @return 入力データ定義
	 */
	public InputDataDefine getInputDataDefine() {
		return inputDataDefine;
	}
	
	/**
	 * 変数を登録する
	 * 
	 * @param name	変数名
	 * @param value	変数値
	 */
	public void registeVar(String name, String value) {
		varMap.put(name, value);
	}
	
	/**
	 * 変数値を取得する
	 * 
	 * @param name	変数名
	 * @return	変数値
	 */
	public String getVar(String name) {
		return varMap.get(name);
	}
	
	/**
	 * タスクを登録する
	 * 
	 * @param task	タスク情報
	 */
	protected void registeTask(Task task) {
		taskList.add(task);
	}
	
	/**
	 * タスクリストを取得する
	 * 
	 * @return	タスクリスト
	 */
	public List<Task> getTaskList() {
		return taskList;
	}
	
	/**
	 * テンプレート定義を追加する
	 * 
	 * @param define	テンプレート定義
	 */
	protected void addTemplateDefine(TemplateDefine define) {
		templateDefineList.add(define);
	}

	/**
	 * テンプレート定義リスト を取得する
	
	 * @return テンプレート定義リスト
	 */
	public List<TemplateDefine> getTemplateDefineList() {
		return templateDefineList;
	}
	
	/**
	 * マッピング定義 を取得する
	
	 * @return マッピング定義
	 */
	public MappingDefine getMappingDefine() {
		return mappingDefine;
	}

	/**
	 * マッピング定義 を設定する
	 *
	 * @param mappingDefine マッピング定義
	 */
	public void setMappingDefine(MappingDefine mappingDefine) {
		this.mappingDefine = mappingDefine;
	}	
		
	
}
