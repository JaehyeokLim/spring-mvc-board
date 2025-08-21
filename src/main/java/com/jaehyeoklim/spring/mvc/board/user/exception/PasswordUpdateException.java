package com.jaehyeoklim.spring.mvc.board.user.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PasswordUpdateException extends RuntimeException {

    private final Reason reason;

    public enum Reason { INVALID_CURRENT, SAME_AS_OLD }
}
