package com.conx.logistics.kernel.expression.impl.spel;

import java.util.Map;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.conx.logistics.kernel.expression.services.IExpressionEngine;
import com.conx.logistics.kernel.expression.services.IExpressionEvaluationContext;

public class ExpressionEngineImpl implements IExpressionEngine{
	private ExpressionParser parser = new SpelExpressionParser();
	
	@Override
	public IExpressionEvaluationContext createContext() {
		StandardEvaluationContextImpl context = new StandardEvaluationContextImpl();
		return context;
	}

	@Override
	public IExpressionEvaluationContext createContext(Map<String, Object> parameters) {
		StandardEvaluationContextImpl context = new StandardEvaluationContextImpl();
		context.setVariables(parameters);
		return context;
	}

	@Override
	public <T> T evaluate(Map<String, Object> parameters, String expressionString, Class<T> type) {
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setVariables(parameters);
		
		return parser.parseExpression(expressionString).getValue(context,type);
	}

	@Override
	public <T> T evaluate(IExpressionEvaluationContext context, String expressionString, Map<String, Object> parameters, Class<T> type) {
		StandardEvaluationContextImpl internalContext = (StandardEvaluationContextImpl)context.cast();
		internalContext.setVariables(parameters);
		
		return parser.parseExpression(expressionString).getValue(internalContext,type);
	}

	@Override
	public <T> T evaluate(String expressionString, Class<T> type) {
		return parser.parseExpression(expressionString).getValue(type);
	}
}
