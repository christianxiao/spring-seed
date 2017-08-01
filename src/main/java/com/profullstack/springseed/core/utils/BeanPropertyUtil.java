package com.profullstack.springseed.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.AbstractEnvironment;

import java.beans.IntrospectionException;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Map;

/**
 * Created by christianxiao on 7/27/17.
 * Modified from: http://blog.csdn.net/huazaichang/article/details/9092281
 */
@Slf4j
public class BeanPropertyUtil {

	public static void setBeanProperty(Object bean, AbstractEnvironment environment, String propertyPrefix) {
		Map<String, String> valueMap = EnvironmentUtil.getAllProperties(environment, propertyPrefix);
		for(Map.Entry<String, String> entry: valueMap.entrySet()){
			setBeanProperty(bean, entry.getKey(), entry.getValue());
		}
	}

	public static void setBeanProperty(Object bean, String prop, String value) {
		try {
			if (System.getSecurityManager() != null) {
				PrivilegedIntrospectHelper dp = new PrivilegedIntrospectHelper(bean, prop, value);
				try {
					AccessController.doPrivileged(dp);
				} catch (PrivilegedActionException e) {
					e.printStackTrace();
				}
			} else {
				internalIntrospecthelper(bean, prop, value);
			}
		} catch (IntrospectionException | InvocationTargetException | IllegalAccessException | InstantiationException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
		}
	}

	private static void internalIntrospecthelper(Object bean,
												 String prop,
												 String value) throws IntrospectionException, InvocationTargetException, IllegalAccessException, InstantiationException {
		Method method = null;
		Class type = null;
		Class propertyEditorClass = null;

		java.beans.BeanInfo info = java.beans.Introspector.getBeanInfo(bean.getClass());
		if (info != null) {
			java.beans.PropertyDescriptor pd[] = info.getPropertyDescriptors();
			for (int i = 0; i < pd.length; i++) {
				if (pd[i].getName().equals(prop)) {
					method = pd[i].getWriteMethod();
					type = pd[i].getPropertyType();
					propertyEditorClass = pd[i].getPropertyEditorClass();
					break;
				}
			}
		}
		if (method != null) {
			if (type.isArray()) {
				Class t = type.getComponentType();
				String[] values = value.trim().split("\\s*,\\s*");
				if (t.equals(String.class)) {
					method.invoke(bean, new Object[] { values });
				} else {
					createTypedArray(prop, bean, method, values, t, propertyEditorClass);
				}
			} else {
				if (value == null || value.equals(""))
					return;
				Object oval = convert(prop, value, type, propertyEditorClass);
				if (oval != null)
					method.invoke(bean, new Object[] { oval });
			}
		}
	}

	private static Object getValueFromBeanInfoPropertyEditor(String attrValue, Class propertyEditorClass)
		throws IllegalAccessException, InstantiationException {
			PropertyEditor pe = (PropertyEditor) propertyEditorClass.newInstance();
			pe.setAsText(attrValue);
			return pe.getValue();

	}

	private static class PrivilegedIntrospectHelper implements PrivilegedExceptionAction {

		private Object bean;

		private String prop;

		private String value;

		PrivilegedIntrospectHelper(Object bean, String prop, String value) {
			this.bean = bean;
			this.prop = prop;
			this.value = value;
		}

		public Object run() throws IntrospectionException, InstantiationException, IllegalAccessException, InvocationTargetException {
			internalIntrospecthelper(bean, prop, value);
			return null;
		}
	}

	private static Object convert(String propertyName, String s, Class t, Class propertyEditorClass) throws InstantiationException, IllegalAccessException {
			if (s == null) {
				if (t.equals(Boolean.class) || t.equals(Boolean.TYPE))
					s = "false";
				else
					return null;
			}
			if (propertyEditorClass != null) {
				return getValueFromBeanInfoPropertyEditor(s, propertyEditorClass);
			} else if (t.equals(Boolean.class) || t.equals(Boolean.TYPE)) {
				if (s.equalsIgnoreCase("true"))
					s = "true";
				else
					s = "false";
				return Boolean.valueOf(s);
			} else if (t.equals(Byte.class) || t.equals(Byte.TYPE)) {
				return new Byte(s);
			} else if (t.equals(Character.class) || t.equals(Character.TYPE)) {
				return s.length() > 0 ? new Character(s.charAt(0)) : null;
			} else if (t.equals(Short.class) || t.equals(Short.TYPE)) {
				return new Short(s);
			} else if (t.equals(Integer.class) || t.equals(Integer.TYPE)) {
				return new Integer(s);
			} else if (t.equals(Float.class) || t.equals(Float.TYPE)) {
				return new Float(s);
			} else if (t.equals(Long.class) || t.equals(Long.TYPE)) {
				return new Long(s);
			} else if (t.equals(Double.class) || t.equals(Double.TYPE)) {
				return new Double(s);
			} else if (t.equals(String.class)) {
				return s;
			} else if (t.equals(java.io.File.class)) {
				return new java.io.File(s);
			} else if (t.getName().equals("java.lang.Object")) {
				return new Object[] { s };
			} else {
				return getValueFromPropertyEditorManager(t, s);
			}
	}

	private static Object getValueFromPropertyEditorManager(Class attrClass, String attrValue) {
		PropertyEditor propEditor = PropertyEditorManager.findEditor(attrClass);
		if (propEditor != null) {
			propEditor.setAsText(attrValue);
			return propEditor.getValue();
		} else {
			throw new IllegalArgumentException("beans property editor not registered");
		}
	}

	private static void createTypedArray(String propertyName, Object bean, Method method, String[] values, Class t, Class propertyEditorClass) throws InvocationTargetException, IllegalAccessException, InstantiationException {

		if (propertyEditorClass != null) {
			Object[] tmpval = new Integer[values.length];
			for (int i = 0; i < values.length; i++) {
				tmpval[i] = getValueFromBeanInfoPropertyEditor(values[i], propertyEditorClass);
			}
			method.invoke(bean, new Object[] { tmpval });
		} else if (t.equals(Integer.class)) {
			Integer[] tmpval = new Integer[values.length];
			for (int i = 0; i < values.length; i++)
				tmpval[i] = new Integer(values[i]);
			method.invoke(bean, new Object[] { tmpval });
		} else if (t.equals(Byte.class)) {
			Byte[] tmpval = new Byte[values.length];
			for (int i = 0; i < values.length; i++)
				tmpval[i] = new Byte(values[i]);
			method.invoke(bean, new Object[] { tmpval });
		} else if (t.equals(Boolean.class)) {
			Boolean[] tmpval = new Boolean[values.length];
			for (int i = 0; i < values.length; i++)
				tmpval[i] = new Boolean(values[i]);
			method.invoke(bean, new Object[] { tmpval });
		} else if (t.equals(Short.class)) {
			Short[] tmpval = new Short[values.length];
			for (int i = 0; i < values.length; i++)
				tmpval[i] = new Short(values[i]);
			method.invoke(bean, new Object[] { tmpval });
		} else if (t.equals(Long.class)) {
			Long[] tmpval = new Long[values.length];
			for (int i = 0; i < values.length; i++)
				tmpval[i] = new Long(values[i]);
			method.invoke(bean, new Object[] { tmpval });
		} else if (t.equals(Double.class)) {
			Double[] tmpval = new Double[values.length];
			for (int i = 0; i < values.length; i++)
				tmpval[i] = new Double(values[i]);
			method.invoke(bean, new Object[] { tmpval });
		} else if (t.equals(Float.class)) {
			Float[] tmpval = new Float[values.length];
			for (int i = 0; i < values.length; i++)
				tmpval[i] = new Float(values[i]);
			method.invoke(bean, new Object[] { tmpval });
		} else if (t.equals(Character.class)) {
			Character[] tmpval = new Character[values.length];
			for (int i = 0; i < values.length; i++)
				tmpval[i] = new Character(values[i].charAt(0));
			method.invoke(bean, new Object[] { tmpval });
		} else if (t.equals(int.class)) {
			int[] tmpval = new int[values.length];
			for (int i = 0; i < values.length; i++)
				tmpval[i] = Integer.parseInt(values[i]);
			method.invoke(bean, new Object[] { tmpval });
		} else if (t.equals(byte.class)) {
			byte[] tmpval = new byte[values.length];
			for (int i = 0; i < values.length; i++)
				tmpval[i] = Byte.parseByte(values[i]);
			method.invoke(bean, new Object[] { tmpval });
		} else if (t.equals(boolean.class)) {
			boolean[] tmpval = new boolean[values.length];
			for (int i = 0; i < values.length; i++)
				tmpval[i] = (Boolean.valueOf(values[i])).booleanValue();
			method.invoke(bean, new Object[] { tmpval });
		} else if (t.equals(short.class)) {
			short[] tmpval = new short[values.length];
			for (int i = 0; i < values.length; i++)
				tmpval[i] = Short.parseShort(values[i]);
			method.invoke(bean, new Object[] { tmpval });
		} else if (t.equals(long.class)) {
			long[] tmpval = new long[values.length];
			for (int i = 0; i < values.length; i++)
				tmpval[i] = Long.parseLong(values[i]);
			method.invoke(bean, new Object[] { tmpval });
		} else if (t.equals(double.class)) {
			double[] tmpval = new double[values.length];
			for (int i = 0; i < values.length; i++)
				tmpval[i] = Double.valueOf(values[i]).doubleValue();
			method.invoke(bean, new Object[] { tmpval });
		} else if (t.equals(float.class)) {
			float[] tmpval = new float[values.length];
			for (int i = 0; i < values.length; i++)
				tmpval[i] = Float.valueOf(values[i]).floatValue();
			method.invoke(bean, new Object[] { tmpval });
		} else if (t.equals(char.class)) {
			char[] tmpval = new char[values.length];
			for (int i = 0; i < values.length; i++)
				tmpval[i] = values[i].charAt(0);
			method.invoke(bean, new Object[] { tmpval });
		} else {
			Object[] tmpval = new Integer[values.length];
			for (int i = 0; i < values.length; i++) {
				tmpval[i] = getValueFromPropertyEditorManager(t, values[i]);
			}
			method.invoke(bean, new Object[] { tmpval });
		}

	}
}
