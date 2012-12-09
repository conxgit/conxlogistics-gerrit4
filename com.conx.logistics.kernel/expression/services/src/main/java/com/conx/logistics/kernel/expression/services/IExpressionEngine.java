package com.conx.logistics.kernel.expression.services;

import java.util.Map;

public interface IExpressionEngine {
	/**
	 * Evaluation methods
	 */	
	public <T> T evaluate(String expressionString, Class<T> type);
	
	public <T> T evaluate(Map<String,Object> parameters, String expressionString, Class<T> type);	
	
	public <T> T evaluate(IExpressionEvaluationContext context, String expressionString, Map<String,Object> parameters, Class<T> type);	
	
	/**
	 * Factory methods
	 */
	public IExpressionEvaluationContext createContext();

	public IExpressionEvaluationContext createContext(Map<String,Object> parameters);	
}
