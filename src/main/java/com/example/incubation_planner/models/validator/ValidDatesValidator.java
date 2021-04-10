package com.example.incubation_planner.models.validator;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ValidDatesValidator implements ConstraintValidator<ValidDates, Object> {
    private String firstField;
    private String secondField;
    private String message;

    @Override
    public void initialize(ValidDates constraintAnnotation) {
        firstField = constraintAnnotation.first();
        secondField = constraintAnnotation.second();
        this.message = constraintAnnotation.message();
    }


    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(value);
        Object firstFieldValue = beanWrapper.getPropertyValue(firstField);
        Object secondFieldValue = beanWrapper.getPropertyValue(secondField);

        boolean valid;
        if (firstFieldValue == null) {
            valid = secondFieldValue == null;
        } else {
            valid = ((LocalDate) firstFieldValue).isBefore((LocalDate) secondFieldValue);
        }

        if (!valid) {
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(secondField)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }
        return valid;
    }
}
