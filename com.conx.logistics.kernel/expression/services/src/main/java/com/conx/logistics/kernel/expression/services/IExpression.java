package com.conx.logistics.kernel.expression.services;

public interface IExpression {
	public <T> T getValue(IExpressionEvaluationContext context, Class<T> type);
}
