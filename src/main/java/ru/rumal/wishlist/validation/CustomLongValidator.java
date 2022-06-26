package ru.rumal.wishlist.validation;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class CustomLongValidator implements ConstraintValidator<CustomLong, Long> {

    private boolean nullable;
    private long min;
    private long max;

    @Override
    public void initialize(CustomLong annotation) {
        this.nullable = annotation.nullable();
        this.min = annotation.min();
        this.max = annotation.max();
    }

    @Override
    public boolean isValid(Long value,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (nullable && value == null) return true;
        if (value == null) return false;
        return value >= min && value <= max;
    }
}
