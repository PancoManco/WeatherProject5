package ru.pancoManco.weatherViewer.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.pancoManco.weatherViewer.annotation.PasswordMatch;
import ru.pancoManco.weatherViewer.dto.UserRegisterDto;

public class PasswordMatchValidator implements ConstraintValidator<PasswordMatch, UserRegisterDto> {

    @Override
    public boolean isValid(UserRegisterDto dto, ConstraintValidatorContext context) {
        if (dto.getPassword()==null || dto.getRepeatPassword()==null) {
            return true;
        }
        boolean match = dto.getPassword().equals(dto.getRepeatPassword());

        if (!match) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate("Пароли не совпадают")
                    .addPropertyNode("repeatPassword")
                    .addConstraintViolation();
        }
        return match;
    }
}
