package com.jaehyeoklim.spring.mvc.board.user.validator;

import com.jaehyeoklim.spring.mvc.board.user.dto.UserSignupRequest;
import com.jaehyeoklim.spring.mvc.board.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class UserSignupRequestValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserSignupRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserSignupRequest request = (UserSignupRequest) target;

        if (userService.isUsernameTaken(request.username())) {
            errors.rejectValue("username", "Duplicate.user.username");
        }

        if (userService.isEmailTaken(request.email())) {
            errors.rejectValue("email", "Duplicate.user.email");
        }
    }
}
