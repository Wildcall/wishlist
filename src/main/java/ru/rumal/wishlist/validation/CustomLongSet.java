package ru.rumal.wishlist.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CustomLongSetValidator.class)
public @interface CustomLongSet {
    String message() default "Invalid input long set field";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean nullable() default false;

    long min() default Long.MIN_VALUE;

    long max() default Long.MAX_VALUE;
}
