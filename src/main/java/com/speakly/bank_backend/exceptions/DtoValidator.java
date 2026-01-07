package com.speakly.bank_backend.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

public class DtoValidator {

    private static Validator validator;

    private static Validator getValidator() {
        if (validator == null) {
            ValidatorFactory factory = Validation.byDefaultProvider()
                    .configure()
                    .buildValidatorFactory();
            validator = factory.getValidator();
        }
        return validator;
    }

    public static <T> void validate(T dto) {
        Set<ConstraintViolation<T>> violations = getValidator().validate(dto);
        if (!violations.isEmpty()) {
            throw new ValidationException(violations);
        }
    }
}