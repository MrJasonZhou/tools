package com.jasonzhou.tool.sag.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jasonzhou.tool.sag.Config;

/**
 * ソース自動生成用設定情報
 * 
 * @author Jason Zhou
 *
 */
@Component
public class SagConfig extends Config {
	
    private static Logger logger = LoggerFactory.getLogger(SagConfig.class);
	
	/** タスク情報 */
	private Tasks tasks ;
	
	/** 入力ファイル情報 */
	private InputFiles inputFiles;

	/**
	 * タスク情報 を取得する
	 *
	 * @return タスク情報
	 */
	public Tasks getTasks() {
		return tasks;
	}

	/**
	 * タスク情報 を設定する
	 *
	 * @param tasks タスク情報
	 */
	public void setTasks(Tasks tasks) {
		this.tasks = tasks;
	}

	/**
	 * 入力ファイル情報 を取得する
	 *
	 * @return 入力ファイル情報
	 */
	public InputFiles getInputFiles() {
		return inputFiles;
	}

	/**
	 * 入力ファイル情報 を設定する
	 *
	 * @param inputFiles 入力ファイル情報
	 */
	public void setInputFiles(InputFiles inputFiles) {
		this.inputFiles = inputFiles;
	}


    
}
