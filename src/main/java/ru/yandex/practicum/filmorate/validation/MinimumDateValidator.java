package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class MinimumDateValidator implements ConstraintValidator<StartRelease, LocalDate> {
    private LocalDate minimumDate;

    @Override
    public void initialize(final StartRelease constraintAnnotation) {
        minimumDate = LocalDate.parse(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(final LocalDate value, final ConstraintValidatorContext context) {
        return value == null || !value.isBefore(minimumDate);
    }
}
