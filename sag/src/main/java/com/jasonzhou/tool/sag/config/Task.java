/**  */
package com.jasonzhou.tool.sag.config;

/**
 * タスク定義情報
 * 
 * @author Jason Zhou
 *
 */
public class Task {

	/** テンプレートファイル名 */
	private String templateFile;
	
	/** スイッチ関数式 */
	private String switchExpress;
	
	/** 出力ファイル */
	private String outputFile;
	
	/**
	 * テンプレートファイル名 を取得する
	
	 * @return テンプレートファイル名
	 */
	public String getTemplateFile() {
		return templateFile;
	}

	/**
	 * テンプレートファイル名 を設定する
	 *
	 * @param templateFile テンプレートファイル名
	 */
	public void setTemplateFile(String templateFile) {
		this.templateFile = templateFile;
	}

	/**
	 * スイッチ関数式 を取得する
	
	 * @return スイッチ関数式
	 */
	public String getSwitchExpress() {
		return switchExpress;
	}

	/**
	 * スイッチ関数 を設定する
	 *
	 * @param switchExpress スイッチ関数式
	 */
	public void setSwitchExpress(String switchExpress) {
		this.switchExpress = switchExpress;
	}

	/**
	 * 出力フォルダ を取得する
	
	 * @return 出力フォルダ
	 */
	public String getOutputFile() {
		return outputFile;
	}

	/**
	 * 出力フォルダ を設定する
	 *
	 * @param outputFile 出力フォルダ
	 */
	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}


}
