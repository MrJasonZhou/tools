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
	
	private Task task = new Task();
    
}
