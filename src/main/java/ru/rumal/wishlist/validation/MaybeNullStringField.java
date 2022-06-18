package ru.rumal.wishlist.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MaybeNullValidator.class)
public @interface MaybeNullStringField {
    String message() default "Invalid input string field";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
