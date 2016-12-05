package com.scsb.vaadin.ui.validator;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.scsb.vaadin.ui.validator.CheckEmpty;
public class ScsbValidator {
	private boolean isNull = true;
	
    @NotNull
    @Min(value=0 ,message="{Integer.Min0_Max99}")
    @Max(value=99,message="{Integer.Min0_Max99}")
    private int Int0_99;
    
    @Min(value=0 ,message="{Float.Min_Max}")
    @Max(value=999999999 ,message="{Float.Min_Max}")
    private float Floatl0_99;
    
    @NotNull
    @CheckEmpty(message="{String.Empty.Error}")
    private String String_CheckEmpty;    
}