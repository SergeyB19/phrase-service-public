package ru.mycompany.phrase.domen.response.exception;

import lombok.Builder;
import lombok.Data;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class CommonException extends RuntimeException {

    private final Code code;
    private final String userMessage;
    private final String techMessage;
    private final HttpStatus httpStatus;
}