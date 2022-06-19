package ru.rumal.wishlist.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CustomStringValidator implements ConstraintValidator<CustomString, CharSequence> {

    private boolean nullable;
    private int min;
    private int max;

    @Override
    public void initialize(CustomString annotation) {
        this.nullable = annotation.nullable();
        this.min = annotation.min();
        this.max = annotation.max();
    }

    @Override
    public boolean isValid(CharSequence charSequence,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (nullable && charSequence == null) return true;
        if (charSequence == null) return false;

        //  @formatter:off
        String s = charSequence.toString();
        return !s.isEmpty() &&
                !s.trim().isEmpty() &&
                s.length() >= min &&
                s.length() <= max;
        //  @formatter:on
    }
}
