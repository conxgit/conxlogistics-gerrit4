package com.conx.logistics.kernel.expression.services;

import java.util.Map;

public interface IExpressionEvaluationContext {
	public Object cast();	
	
	public void setVariable(String variableName, Object value);

	public void setVariables(Map<String, Object> variables);
}
