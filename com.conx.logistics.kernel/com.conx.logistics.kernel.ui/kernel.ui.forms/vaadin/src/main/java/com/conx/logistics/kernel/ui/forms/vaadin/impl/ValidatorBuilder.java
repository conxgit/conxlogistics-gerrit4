package com.conx.logistics.kernel.ui.forms.vaadin.impl;

import com.conx.logistics.kernel.datasource.domain.validators.NotNullValidator;
import com.conx.logistics.kernel.ui.forms.vaadin.impl.validators.VaadinNotNullValidator;
import com.vaadin.data.Validator;

public class ValidatorBuilder {
	public static Validator create(com.conx.logistics.kernel.datasource.domain.validators.Validator validator, VaadinForm form) {
		if (validator instanceof NotNullValidator) {
			return new VaadinNotNullValidator();
		}
		return null;
	}
}
