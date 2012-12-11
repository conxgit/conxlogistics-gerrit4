package com.conx.logistics.kernel.ui.forms.vaadin.impl.validators;

import com.vaadin.data.Validator;

public class VaadinNotNullValidator implements Validator {
	private static final long serialVersionUID = -5667981386201422656L;

	@Override
	public void validate(Object value) throws InvalidValueException {
		if (value == null) {
			throw new InvalidValueException("The value of this field cannot be null.");
		}
	}

	@Override
	public boolean isValid(Object value) {
		return value != null;
	}

}
