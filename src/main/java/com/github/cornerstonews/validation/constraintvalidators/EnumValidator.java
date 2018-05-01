package com.github.cornerstonews.validation.constraintvalidators;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.github.cornerstonews.validation.constraints.ValidEnum;

public class EnumValidator implements ConstraintValidator<ValidEnum, Enum<?>> {

    private Class<? extends Enum<?>> type;

    @Override
    public void initialize(ValidEnum value) {
        this.type = value.type();
    }

    /**
     * This method assumes that the enum that utilizes this validator is required, aka not nullable.
     * It is impossible to determine the validity of the input if the enum is nullable,
     * since it cannot be distinguished whether the null input is due to bad input or no input.
     *
     * @param input The enum to validate.
     * @param constraintValidatorContext constraintValidatorContext
     * @return Whether the enum is valid.
     */
    @Override
    public boolean isValid(Enum<?> input, ConstraintValidatorContext constraintValidatorContext) {

        // If input is null, it is either invalid or not provided.
        // If it is not null, it is guaranteed to be valid.
        if (input == null) {
            String message = "Valid input required. Options: " + this.join(type);
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
            return false;
        }

        return true;
    }
    
    private String join(Class<? extends Enum<?>> clazz) {
        List<String> enums = Arrays.asList(Arrays.stream(clazz.getEnumConstants()).map(e -> e.toString()).toArray(String[]::new));
        return String.join(", ", enums);
    }

}
