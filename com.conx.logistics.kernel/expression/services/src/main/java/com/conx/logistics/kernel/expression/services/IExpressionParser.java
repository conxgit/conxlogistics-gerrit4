package com.conx.logistics.kernel.expression.services;

public interface IExpressionParser {
	public IExpression parseExpression(String expressionString, IExpressionEvaluationContext context);
}
