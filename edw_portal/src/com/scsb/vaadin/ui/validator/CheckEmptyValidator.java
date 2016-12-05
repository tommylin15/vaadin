package com.scsb.vaadin.ui.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckEmptyValidator implements ConstraintValidator <CheckEmpty, String> { 
 private boolean isNull = false;
  public void initialize(CheckEmpty parameters) { 
  } 
  public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
     if (value == null) {
         return isNull;
     }
     if (value.equals("")) return isNull;
     return true;
  } 
}