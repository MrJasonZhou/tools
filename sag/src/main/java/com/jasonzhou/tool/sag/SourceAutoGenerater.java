/**
 *
 */
package com.jasonzhou.tool.sag;

import org.apache.commons.lang3.StringUtils;

import com.jasonzhou.tool.sag.config.SagConfig;
import com.jasonzhou.tool.sag.util.SagUtil;

/**
 * ソース自動生成
 *
 * @author Jason Zhou
 *
 */
public class SourceAutoGenerater {

	/**
	 * ソース自動生成
	 *
	 * @param config	設定情報
	 * @throws Exception
	 */
	public void execute(SagConfig config) throws Exception {
		String clsName = config.getProperty("templateEngineer.class");
		ITemplateEngineer te = null;
		if (StringUtils.isNotBlank(clsName)) {
			Class<ITemplateEngineer> cls = (Class<ITemplateEngineer>)Class.forName(clsName);
			te = SagUtil.newInstance(cls);
			te.execute(config);
		}
	}

}
