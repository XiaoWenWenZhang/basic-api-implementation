package com.thoughtworks.rslist.component;

import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class RsEventHandler {
    @ExceptionHandler({RsEventNotValidException.class, MethodArgumentNotValidException.class, IndexOutOfBoundsException.class})
    public ResponseEntity rsExceptionHandler(HttpServletRequest httpServletRequest, Exception e) {
        String errorMessage;
        Error error = new Error();
        if (e instanceof MethodArgumentNotValidException && httpServletRequest.getRequestURI().equals("/user")) {
            errorMessage = "invalid user";
        } else if(e instanceof RsEventNotValidException){
            errorMessage = e.getMessage();
        }else {
            errorMessage = "invalid request param";
        }
        error.setError(errorMessage);
        return ResponseEntity.badRequest().body(error);
    }
}
