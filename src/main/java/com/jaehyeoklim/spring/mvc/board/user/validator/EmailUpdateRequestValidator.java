package com.jaehyeoklim.spring.mvc.board.user.validator;

import com.jaehyeoklim.spring.mvc.board.user.dto.EmailUpdateRequest;
import com.jaehyeoklim.spring.mvc.board.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class EmailUpdateRequestValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return EmailUpdateRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EmailUpdateRequest request = (EmailUpdateRequest) target;

        if (userService.isEmailTaken(request.email())) {
            errors.rejectValue("email", "Duplicate.user.email");
        }
    }
}
