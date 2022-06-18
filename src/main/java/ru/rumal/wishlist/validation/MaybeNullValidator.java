package ru.rumal.wishlist.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MaybeNullValidator implements ConstraintValidator<MaybeNullStringField, CharSequence> {

    @Override
    public boolean isValid(CharSequence charSequence,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (charSequence == null)
            return true;
        return false;
    }
}
