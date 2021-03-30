package com.example.incubation_planner.models.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDateTime;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy= ValidDatesValidator.class)
public @interface ValidDates {

    String first();
    String second();

    String message() default "The end date must be after the starting date and both must be in the future";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
