package com.conx.logistics.kernel.ui.forms.vaadin.impl.validators;

import java.util.HashSet;
import java.util.Set;

import com.conx.logistics.kernel.datasource.domain.validators.ExpressionValidator;
import com.conx.logistics.kernel.datasource.domain.validators.expression.ExpressionOperator;
import com.conx.logistics.kernel.ui.forms.vaadin.impl.VaadinForm;
import com.conx.logistics.kernel.ui.forms.vaadin.impl.ValidatorBuilder;
import com.vaadin.data.Validator;

public class VaadinExpressionValidator implements Validator {
	private static final long serialVersionUID = 5361621586755600223L;
	
	private ExpressionValidator dataSourceValidator;
	private VaadinForm form;
	private Set<Validator> validatorSet;
	
	public VaadinExpressionValidator(ExpressionValidator dataSourceValidator, VaadinForm form) {
		this.dataSourceValidator = dataSourceValidator;
		this.form = form;
		this.validatorSet = new HashSet<Validator>();
		
		for (com.conx.logistics.kernel.datasource.domain.validators.Validator validator : this.dataSourceValidator.getValidatorSet()) {
			this.validatorSet.add(ValidatorBuilder.create(validator, this.form));
		}
	}
	
	private boolean evaluate(Object value) {
		for (Validator validator : this.validatorSet) {
			if (!validator.isValid(value)) {
				if (this.dataSourceValidator.getOperator() == ExpressionOperator.AND) {
					return false;
				}
			} else {
				if (this.dataSourceValidator.getOperator() == ExpressionOperator.OR) {
					return true;
				}
			}
		}
		
		if (this.dataSourceValidator.getOperator() == ExpressionOperator.AND) {
			return true;
		} else if (this.dataSourceValidator.getOperator() == ExpressionOperator.OR) {
			return false;
		}
		
		return false;
	}

	@Override
	public void validate(Object value) throws InvalidValueException {
		if (!evaluate(value)) {
			throw new InvalidValueException("This value is invalid");
		}
	}

	@Override
	public boolean isValid(Object value) {
		return evaluate(value);
	}

}
