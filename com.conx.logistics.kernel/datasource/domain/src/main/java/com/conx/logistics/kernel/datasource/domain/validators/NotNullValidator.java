package com.conx.logistics.kernel.datasource.domain.validators;

import javax.persistence.Entity;

@Entity
public class NotNullValidator extends Validator {
	
	public NotNullValidator() {
		typeAsString = "notnull";
	}
}
