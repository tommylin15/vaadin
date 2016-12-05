package com.scsb.python;

import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;

import com.vaadin.ui.VerticalLayout;

public class JythonTest  {
	private static PythonInterpreter interpreter;

	public static void main(String args[]){
		interpreter = new PythonInterpreter();
		interpreter.exec("days=('mod','Tue','Wed','Thu','Fri','Sat','Sun'); ");
		interpreter.exec("print days[1];");
		/*
		interpreter.exec("from sas7bdat import SAS7BDAT");
		interpreter.exec("with SAS7BDAT('d:/ftp/ibsps/fnbct0.sas7bdat') as f:");
		interpreter.exec("for row in f:");
		interpreter.exec("	print row");
		*/
		/*
		 * 
		 * from sas7bdat import SAS7BDAT
with SAS7BDAT('foo.sas7bdat') as f:
    for row in f:
        print row
        // Jython Object
        PyObject pyObject = JythonFactory.getInstance().getPyObjectFromJythonFile("Employee", "d://python/Employee.py");
        System.out.println("+++="+pyObject.invoke("getEmployeeId"));
        pyObject.invoke("setEmployeeId",new PyString("1999"));
        System.out.println("+++="+pyObject.invoke("getEmployeeId"));
        
        // Jython Function
        PyFunction pyFunction = JythonFactory.getInstance().getPyFunctionFromJythonFile("getNunmberValue", "d://python/Employee.py");
        System.out.println("***="+pyFunction.__call__(new PyInteger(10)));
        
        //Jython vaadin (Object)
        PyObject pyObject2 = JythonFactory.getInstance().getPyObjectFromJythonFile("PyComponent", "d://python/PyComponent.py");
        VerticalLayout pyApp = (VerticalLayout)pyObject2.__tojava__(VerticalLayout.class);
        */
	}
}