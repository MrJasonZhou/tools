/**  */
package com.jasonzhou.tool.sag.config;

/**
 * テンプレート定義
 * 
 * @author Jason Zhou
 *
 */
public class TemplateDefine {
	
	/** テンプレートファイル名 */
	private String templateFileName;
	
	/** スイッチ関数式 */
	private String switchExpress;
	
	/** 出力ファイル */
	private String outputFile;
	
	/**
	 * テンプレートファイル名 を取得する
	
	 * @return テンプレートファイル名
	 */
	public String getTemplateFileName() {
		return templateFileName;
	}

	/**
	 * テンプレートファイル名 を設定する
	 *
	 * @param templateFileName テンプレートファイル名
	 */
	public void setTemplateFileName(String templateFileName) {
		this.templateFileName = templateFileName;
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
