package com.conx.logistics.kernel.expression.impl.spel;

import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.conx.logistics.kernel.expression.services.IExpressionEvaluationContext;

public class StandardEvaluationContextImpl implements IExpressionEvaluationContext {
	private StandardEvaluationContext context = new StandardEvaluationContext();
	
	@Override
	public void setVariable(String variableName, Object value) {
		context.setVariable(variableName, value);
	}
	
	@Override
	public void setVariables(Map<String,Object> variables) {
		context.setVariables(variables);
	}

	@Override
	public Object cast() {
		return context;
	}

	@Override
	public void registerMethod(String name, Method method) {
		this.context.registerFunction(name, method);
	}	
}
