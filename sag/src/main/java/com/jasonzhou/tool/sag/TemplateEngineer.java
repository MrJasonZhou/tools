/**  */
package com.jasonzhou.tool.sag;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.jasonzhou.tool.sag.config.Config;
import com.jasonzhou.tool.sag.config.Task;
import com.jasonzhou.tool.sag.config.TemplateDefine;
import com.jasonzhou.tool.sag.func.Functions;
import com.jasonzhou.tool.sag.info.ExcelInputFile;
import com.jasonzhou.tool.sag.info.InputData;
import com.jasonzhou.tool.sag.template.FreeMarkerTemplate;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * テンプレートエンジン
 * 
 * @author Jason Zhou
 *
 */
public class TemplateEngineer {

	/**
	 * テンプレートからソースを生成する
	 * 
	 * @param config	設定情報
	 * @param define	テンプレート定義情報
	 * @throws IOException 
	 * @throws TemplateException 
	 */
	public static void generate(Config config, TemplateDefine define) throws IOException, TemplateException {
		for (Task task : config.getTaskList()) {
			generate(config, task, define);
		}
	}
	
	/**
	 * テンプレートからソースを生成する
	 * 
	 * @param config	設定情報
	 * @param task	タスク
	 * @param define	テンプレート定義情報
	 * @throws IOException 
	 * @throws TemplateException 
	 */
	private static void generate(Config config, Task task, TemplateDefine define) throws IOException, TemplateException {
		List<InputData> list = ExcelInputFile.parse(config, task);
		FreeMarkerTemplate fmt = FreeMarkerTemplate.createInstance(config.getVar(Config.TEMPLATE_DIR));
		for (InputData inputData : list) {
			Map<String, Object> map = inputData.toMap();
			boolean skip = false;
			if (StringUtils.isNotBlank(define.getSwitchExpress())) {
				Object result = Functions.exexcute(define.getSwitchExpress(), config, task, inputData, null);
				if (result != null && result instanceof Boolean && !(Boolean)result) {
					skip = true;
				}
			}
			if (!skip) {
				Template template = fmt.getTemplate(define.getTemplateFileName());
				//出力ファイル名を評価する
				String fileName = fmt.eval(define.getOutputFile(), map);
				fmt.genSource(template, map, new File(fileName));
			}
		}
	}
}
