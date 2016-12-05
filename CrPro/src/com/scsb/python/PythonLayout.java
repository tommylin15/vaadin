package com.scsb.python;

import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import com.scsb.crpro.CrproLayout;

import com.vaadin.ui.VerticalLayout;

public class PythonLayout extends CrproLayout {
	private PythonInterpreter interpreter;

	public PythonLayout() {
		/*
		interpreter = new PythonInterpreter();
		interpreter.exec("days=('mod','Tue','Wed','Thu','Fri','Sat','Sun'); ");
		interpreter.exec("print days[1];");
		*/
        PyObject pyObject2 = JythonFactory.getInstance().getPyObjectFromJythonFile("PyComponent", "d://python/PyComponent.py");
        VerticalLayout pyLayout = (VerticalLayout)pyObject2.__tojava__(VerticalLayout.class);
		
        this.addComponent(pyLayout);

	}
}