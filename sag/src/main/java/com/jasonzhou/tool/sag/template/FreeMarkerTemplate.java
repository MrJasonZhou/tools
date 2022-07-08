/**  */
package com.jasonzhou.tool.sag.template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;

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
public class FreeMarkerTemplate {

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
    
    /**
     * テキストを評価する
     * 
     * @param sourceCode	評価対象テキスト
     * @param model	データモデル
     * @return	評価結果
     * @throws TemplateException
     * @throws IOException
     */
    public String eval(String sourceCode, Object model) throws TemplateException, IOException {
    	StringWriter writer = new StringWriter();
		Template temp = new Template("eval", sourceCode, config);
		temp.process(model, writer);
		return writer.toString();
    }
}
