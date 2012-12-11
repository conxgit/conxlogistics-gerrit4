package com.conx.logistics.kernel.expression.impl.spel.tests;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.conx.logistics.common.utils.StringUtil;
import com.conx.logistics.kernel.expression.services.IExpressionEngine;
import com.conx.logistics.kernel.expression.services.IExpressionEvaluationContext;

@ContextConfiguration(locations = { "/META-INF/spring/module-context.xml"
})
public class CustomFunctionsTests extends AbstractTestNGSpringContextTests {
	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private IExpressionEngine expressionEngine;

	@BeforeClass
	public void setUp() throws Exception {
		Assert.assertNotNull(applicationContext);
		Assert.assertNotNull(expressionEngine);
	}

	@AfterClass
	public void tearDown() throws Exception {
	}
	
	@Test(enabled=true)
	public void testIsNumber() throws Exception {
		Assert.assertTrue(StringUtil.isNumber("8.34537"));
		Assert.assertTrue(StringUtil.isNumber("11112"));
	}
	
	@Test
	public void testIsNumberExpression() throws Exception {
		FormField volume = new FormField("volume",null,true);
		volume.setValue("87432656");
		
		Set<FormField> ffs = new HashSet<CustomFunctionsTests.FormField>();
		ffs.add(volume);
		
		Form form = new Form(ffs);
		
		Map<String, Object> contextParameters = new HashMap<String, Object>();
		contextParameters.put("form", form);		
		IExpressionEvaluationContext context = expressionEngine.createContext(contextParameters);
		context.registerMethod("isNumber", StringUtil.class.getDeclaredMethod("isNumber", new Class[] { String.class }));
		
		//-- W/o Form
		Assert.assertTrue(expressionEngine.evaluate(context, "#isNumber('8.0')", contextParameters, Boolean.class));
		
		
		//-- With Form
		Assert.assertTrue(expressionEngine.evaluate(context, "#isNumber(#form.getField('volume').getValue())", contextParameters, Boolean.class));
	}	
	
	@Test(enabled=true)
	public void testToNumber() throws Exception {
		Assert.assertEquals(StringUtil.toNumber("8.34537"), 8.34537f);
		Assert.assertEquals(StringUtil.toNumber("11112"), 11112);
	}
	
	@Test(enabled=true)
	public void testToNumberExpression() throws Exception {
		FormField volume = new FormField("volume",null,true);
		volume.setValue("8.88");
		
		Set<FormField> ffs = new HashSet<CustomFunctionsTests.FormField>();
		ffs.add(volume);
		
		Form form = new Form(ffs);
		
		Map<String, Object> contextParameters = new HashMap<String, Object>();
		contextParameters.put("form", form);
		IExpressionEvaluationContext context = expressionEngine.createContext(contextParameters);
		context.registerMethod("toNumber", StringUtil.class.getMethod("toNumber", String.class));
		
		Assert.assertEquals(expressionEngine.evaluate(context, "#toNumber(#form.getField('volume').getValue())", contextParameters, Number.class), 8.88f);		
	}
	
	@Test
	public void testMultiplication() throws Exception {
		FormField length = new FormField("length",null,true);
		length.setValue("2");
		FormField width = new FormField("width",null,true);
		width.setValue("4");
		FormField height = new FormField("height",null,true);
		height.setValue("8");
		
		Set<FormField> ffs = new HashSet<CustomFunctionsTests.FormField>();
		ffs.add(length);
		ffs.add(width);
		ffs.add(height);
		
		Form form = new Form(ffs);
		
		Map<String, Object> contextParameters = new HashMap<String, Object>();
		contextParameters.put("form", form);
		IExpressionEvaluationContext context = expressionEngine.createContext(contextParameters);
		context.registerMethod("toNumber", StringUtil.class.getDeclaredMethod("toNumber", new Class[] { String.class }));
		context.registerMethod("isNumber", StringUtil.class.getDeclaredMethod("isNumber", new Class[] { String.class }));
		// Testing multiplication without the ternary operator
		Assert.assertEquals(expressionEngine.evaluate(context, "#toNumber(#form.getField('length').getValue()) * #toNumber(#form.getField('width').getValue()) * #toNumber(#form.getField('height').getValue())", contextParameters, Number.class), 64);
		
		// Testing ternary operator without multiplication
		Assert.assertEquals(expressionEngine.evaluate(context, "#isNumber(#form.getField('length').getValue().toString()) ? #toNumber(#form.getField('length').getValue().toString()) : 0", contextParameters, Number.class), 2);
	}
	
	class Form {
		private Map<String,FormField> fieldMap = new HashMap<String, CustomFunctionsTests.FormField>();

		public Form(Set<FormField> fields) {
			for (FormField ff : fields)
			{
				fieldMap.put(ff.getName(),ff);
			}
		}

		public FormField getField(String name) {
			return fieldMap.get(name);
		}
		
		public Object getFieldValue(String name) {
			return fieldMap.get(name).getValue();
		}
	}	
	
	class FormField {
		private String name = null;
		private Object value = null;
		private Boolean isValid = true;
		
		
		public FormField(String name, Object value, Boolean isValid) {
			this.name = name;
			this.value = value;
			this.isValid = isValid;
		}
		
		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public Boolean getIsValid() {
			return isValid;
		}

		public String getName() {
			return name;
		}
	}
}
