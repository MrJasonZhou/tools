package com.jasonzhou.tool.sag;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;

import com.jasonzhou.tool.sag.config.ExcelConfig;
import com.jasonzhou.tool.sag.config.TemplateDefine;
import com.jasonzhou.tool.sag.util.SpringUtil;

import freemarker.template.TemplateException;

@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class SagApplication {
	
	private static void execute(String[] args) throws IOException, TemplateException {
		if (args.length < 1) {
			System.out.println("usage : [設定EXCELファイル]");
			return ;
		}
		String configFileName = args[0];
		//設定ファイル
		ExcelConfig config = new ExcelConfig();
		config.load(configFileName);
		for (TemplateDefine templateDefine : config.getTemplateDefineList()) {
//			//ソースを生成する
			TemplateEngineer.generate(config, templateDefine);
		}
	}
	
	public static void main(String[] args) throws Exception {
		ApplicationContext ac = SpringApplication.run(SagApplication.class, args);
		SpringUtil.setApplicationContext(ac);
		try {
			execute(args);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
