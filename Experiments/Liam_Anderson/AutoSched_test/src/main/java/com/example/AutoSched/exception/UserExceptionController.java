package com.example.AutoSched.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionController {
    @ExceptionHandler(value = UserTakenException.class)
    public ResponseEntity<Object> exception(UserTakenException exception) {
        return new ResponseEntity<>("Username has been taken", HttpStatus.IM_USED);
    }

    @ExceptionHandler(value = InvalidUsernameOrPasswordException.class)
    public ResponseEntity<Object> exception(InvalidUsernameOrPasswordException exception) {
        return new ResponseEntity<>("Invalid username or password", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidAuthenticationException.class)
    public ResponseEntity<Object> exception(InvalidAuthenticationException exception) {
        return new ResponseEntity<>("Invalid Session token", HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
    }

    @ExceptionHandler(value = ExpiredSessionException.class)
    public ResponseEntity<Object> exception(ExpiredSessionException exception) {
        return new ResponseEntity<>("Session token expired. Please log back in", HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
    }

}