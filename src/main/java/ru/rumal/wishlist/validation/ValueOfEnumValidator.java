package ru.rumal.wishlist.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, CharSequence> {
    private List<String> acceptedValues;

    @Override
    public void initialize(ValueOfEnum annotation) {
        acceptedValues = Stream
                .of(annotation
                            .enumClass()
                            .getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isValid(CharSequence chars,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (chars == null)
            return true;
        return acceptedValues.contains(chars);
    }
}
