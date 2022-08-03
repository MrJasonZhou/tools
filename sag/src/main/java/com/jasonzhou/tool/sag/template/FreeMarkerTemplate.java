/**  */
package com.jasonzhou.tool.sag.template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import com.jasonzhou.tool.sag.Config;
import com.jasonzhou.tool.sag.ITemplateEngineer;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.NullCacheStorage;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * テンプレート設定情報
 *
 * @author Jason Zhou
 *
 */
public class FreeMarkerTemplate<C extends Config> implements ITemplateEngineer<C> {

	private Configuration config = new Configuration(Configuration.VERSION_2_3_30);

	/**
	 * コンストラクター
	 *
	 * @param path	テンプレートファイルのパス
	 * @throws IOException
	 */
	private FreeMarkerTemplate(String path) throws IOException {
		config.setTemplateLoader(new FileTemplateLoader(new File(path)));
		config.setDefaultEncoding("UTF-8");
		config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		config.setCacheStorage(NullCacheStorage.INSTANCE);
	}

	/**
	 * インスタンスを生成する
	 *
	 * @param path	テンプレートファイルのパス
	 * @return	テンプレート設定情報のインスタンス
	 * @throws IOException
	 */
	public static FreeMarkerTemplate createInstance(String path) throws IOException {
		return new FreeMarkerTemplate(path);
	}

	/**
	 *	キャッシュをクリアする
	 */
	public void clearCache() {
		config.clearTemplateCache();
	}

	/**
	 * テンプレート取得
	 *
	 * @param templateName	テンプレートファイル名
	 * @return	テンプレート
	 * @throws IOException
	 */
	public Template getTemplate(String templateName) throws IOException {
		return config.getTemplate(templateName);
	}

	/**
	 * ソースを生成する
	 *
	 * @param template		テンプレート
	 * @param model	データモデル
	 * @param file	出力ファイル
	 * @throws TemplateException
	 * @throws IOException
	 */
	public void genSource(Template template, Object model, File file) throws TemplateException, IOException {
		template.process(model, new OutputStreamWriter(new FileOutputStream(file)));
	}

	@Override
	public void execute(C config) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Object eval(String express, Object model) throws Exception {
		StringWriter writer = new StringWriter();
		Template temp = new Template("eval", express, config);
		temp.process(model, writer);
		return writer.toString();
	}
}
