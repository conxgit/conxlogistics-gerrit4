package com.conx.logistics.kernel.datasource.domain.validators;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.conx.logistics.kernel.datasource.domain.validators.expression.ExpressionOperator;

@Entity
public class ExpressionValidator extends Validator {
	
	private ExpressionOperator operator;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Validator> validatorSet = new HashSet<Validator>();
	
	public ExpressionValidator() {
		typeAsString = "expression";
		this.setType(ValidatorType.ISEXPRESSION);
	}

	public Set<Validator> getValidatorSet() {
		return validatorSet;
	}

	public void setValidatorSet(Set<Validator> validatorSet) {
		this.validatorSet = validatorSet;
	}

	public ExpressionOperator getOperator() {
		return operator;
	}

	public void setOperator(ExpressionOperator operator) {
		this.operator = operator;
	}
}
