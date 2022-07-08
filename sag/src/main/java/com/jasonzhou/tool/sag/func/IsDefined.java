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
 * 一行に複数ロープ変数は定義された
 * 
 * @author Jason Zhou
 *
 */
@Component
public class IsDefined implements IFunction {

	@Override
	public Object execute(Config config, Task task, InputData data, Map<String, Variable> record, Object... args) {
		if (args == null || args.length == 0 || args[0] == null) {
			return Boolean.FALSE;
		}
		for (Object arg : args) {
			Variable var = record.get(arg.toString());
			if (var != null) {
				String val = var.getValue();
				if (StringUtils.isAllBlank(val)) {
					return Boolean.FALSE;
				}
			}
		}
		return Boolean.TRUE;
	}

}
