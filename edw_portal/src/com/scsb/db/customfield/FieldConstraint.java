package com.scsb.db.customfield;

import java.io.Serializable;

public class FieldConstraint implements Serializable{
    private String fieldType="TextField";
    private String fieldRequired="N";
    private String fieldObj="";
    private String ccCodeKind="";

    public FieldConstraint() {
    }

    public String getFieldType() {
        return this.fieldType;
    }  
    public void setFieldType(String fieldType) {
        this.fieldType=fieldType;
        if (fieldType.equals("NumberText")) setFieldObj("com.scsb.loan.include.NumberText");
        if (fieldType.equals("DoubleText")) setFieldObj("com.scsb.loan.include.DoubleText");
        if (fieldType.equals("CcCodeCombox")) setFieldObj("com.scsb.loan.include.CcCodeCombox");
        if (fieldType.equals("DateField")) setFieldObj("com.scsb.loan.customfield.NewDateField");
        
    }
    
    public String getFieldObj() {
        return this.fieldObj;
    }  
    public void setFieldObj(String fieldObj) {
        this.fieldObj=fieldObj;
    }  
    
    public String getCcCodeKind() {
        return this.ccCodeKind;
    }  
    public void setCcCodeKind(String ccCodeKind) {
        this.ccCodeKind=ccCodeKind;
    }  
    
    public String getFieldRequired() {
        return this.fieldRequired;
    }  
    public void setFieldRequired(String fieldRequired) {
        this.fieldRequired=fieldRequired;
    }     
}
