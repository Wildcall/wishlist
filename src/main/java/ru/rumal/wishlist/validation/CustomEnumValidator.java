package ru.rumal.wishlist.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomEnumValidator implements ConstraintValidator<CustomEnum, Object> {

    private List<String> acceptedValues;
    private boolean nullable;

    @Override
    public void initialize(CustomEnum annotation) {
        this.nullable = annotation.nullable();
        acceptedValues = Stream
                .of(annotation
                            .enumClass()
                            .getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(Object o,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (nullable && o == null) return true;
        if (o == null) return false;

        return acceptedValues.contains(o.toString());
    }
}
