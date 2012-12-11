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

import com.conx.logistics.kernel.expression.services.IExpressionEngine;

@ContextConfiguration(locations = { "/META-INF/spring/module-context.xml"
})
public class ExpressionEngineTests extends AbstractTestNGSpringContextTests {
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
	
	@Test
	public void testOnePlusOne() throws Exception {
		Integer res = expressionEngine.evaluate("1 + 1",Integer.class);
		Assert.assertEquals(res,new Integer(2));
	}
	
	@Test
	public void testAddDoubleVariables() throws Exception {
		Map<String,Object> variables = new HashMap<String, Object>();
		variables.put("len",new Double(3.0));
		variables.put("width",new Double(4.0));
		variables.put("height",new Double(5.0));
		
		Double res = expressionEngine.evaluate(variables,"#len*#width*#height",Double.class);
		Assert.assertEquals(res,new Double(60.0));
	}	
	
	@Test
	public void testFormLikeValidation() throws Exception {
		FormField volume = new FormField("volume",null,true);
		FormField len = new FormField("len",null,true);
		FormField width = new FormField("width",null,true);
		FormField height = new FormField("height",null,true);
		
		Set<FormField> ffs = new HashSet<ExpressionEngineTests.FormField>();
		ffs.add(volume);
		ffs.add(len);
		ffs.add(width);
		ffs.add(height);
		
		Form form = new Form(ffs);
		
		Map<String,Object> variables = new HashMap<String, Object>();
		variables.put("form",form);

		FormField vFF = expressionEngine.evaluate(variables,"#form.getField('volume')",FormField.class);
		Assert.assertNotNull(vFF);
		
		Boolean vNotNull = expressionEngine.evaluate(variables,"#form.getField('volume').getValue() != null",Boolean.class);
		Assert.assertEquals(vNotNull, Boolean.FALSE);	
		
		//Vol Not Valid
		Boolean isVolValid = expressionEngine.evaluate(variables,"#form.getField('volume').getValue() != null OR (#form.getField('width').getValue() != null AND #form.getField('len').getValue() != null)",Boolean.class);
		Assert.assertEquals(isVolValid, Boolean.FALSE);		
		
		//Vol Valid: vol != null
		volume.setValue(new Double(60.0));
		Boolean volValid1 = expressionEngine.evaluate(variables,"#form.getField('volume').getValue() != null OR (#form.getField('width').getValue() != null AND #form.getField('len').getValue() != null)",Boolean.class);
		Assert.assertEquals(volValid1, Boolean.TRUE);
		
		//Vol Valid: len != null and width != null
		volume.setValue(null);
		len.setValue(new Double(3.0));
		width.setValue(new Double(4.0));
		Boolean volValid2 = expressionEngine.evaluate(variables,"#form.getField('volume').getValue() != null OR (#form.getField('width').getValue() != null AND #form.getField('len').getValue() != null)",Boolean.class);
		Assert.assertEquals(volValid2, Boolean.TRUE);			
	}	
	
	class Form {
		private Set<FormField> fields;
		private Map<String,FormField> fieldMap = new HashMap<String, ExpressionEngineTests.FormField>();

		public Form(Set<FormField> fields) {
			this.fields = fields;
			
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
