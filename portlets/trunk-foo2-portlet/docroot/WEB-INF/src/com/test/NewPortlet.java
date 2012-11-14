package com.test;

import com.liferay.counter.service.CounterLocalServiceUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;
import com.test.model.Foo;
import com.test.service.FooLocalServiceUtil;

import javax.portlet.PortletException;

/**
 * Portlet implementation class NewPortlet
 */
public class NewPortlet extends MVCPortlet {
 

	@Override
	public void init() throws PortletException {
		try {
			Foo foo = FooLocalServiceUtil.createFoo(CounterLocalServiceUtil.increment());
			foo.setField1("test's");
			foo.setUserName("test's");

			System.out.println("Original");
			System.out.println(foo.getClass().getName());
			System.out.println(foo.getField1() + "\n");

			foo = foo.toEscapedModel();

			System.out.println("Escaped");
			System.out.println(foo.getClass().getName());
			System.out.println(foo.getField1() + "\n");

			foo = foo.toUnescapedModel();

			System.out.println("Unescaped");
			System.out.println(foo.getClass().getName());
			System.out.println(foo.getField1() + "\n");

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
