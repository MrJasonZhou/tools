/**  */
package com.jasonzhou.tool.sag.func;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.jasonzhou.tool.sag.config.Config;
import com.jasonzhou.tool.sag.config.Task;
import com.jasonzhou.tool.sag.info.InputData;
import com.jasonzhou.tool.sag.info.Variable;

/**
 * 指定されたループ変数は複数定義された
 * 
 * @author Jason Zhou
 *
 */
@Component
public class IsMultiDefined implements IFunction {

	@Override
	public Object execute(Config config, Task task, InputData data, Map<String, Variable> record, Object... args) {
		if (args == null || args.length == 0 || args[0] == null) {
			return Boolean.FALSE;
		}
		String value = args[0].toString();
		int count = 0;
		for (Map<String, Variable> loop : data.getLoopList()) {
			Variable var = loop.get(value);
			if (var != null) {
				if (StringUtils.isNotBlank(var.getValue())) {
					count++;
				}
			}
		}
		return count > 1;
	}

}
