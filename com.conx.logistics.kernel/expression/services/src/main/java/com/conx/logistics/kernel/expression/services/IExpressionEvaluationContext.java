package com.conx.logistics.kernel.expression.services;

import java.lang.reflect.Method;
import java.util.Map;

public interface IExpressionEvaluationContext {
	public Object cast();	
	
	public void setVariable(String variableName, Object value);

	public void setVariables(Map<String, Object> variables);
	
	public void registerMethod(String name, Method method);
}
