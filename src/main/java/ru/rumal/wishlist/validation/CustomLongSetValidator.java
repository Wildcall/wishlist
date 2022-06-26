package ru.rumal.wishlist.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Set;

public class CustomLongSetValidator implements ConstraintValidator<CustomLongSet, Set<Long>> {

    private boolean nullable;
    private long min;
    private long max;

    @Override
    public void initialize(CustomLongSet annotation) {
        this.nullable = annotation.nullable();
        this.min = annotation.min();
        this.max = annotation.max();
    }

    @Override
    public boolean isValid(Set<Long> value,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (nullable && value == null) return true;
        if (value == null) return false;
        for (Long o : value) {
            if (o < min && o > max)
                return false;
        }
        return true;
    }
}
