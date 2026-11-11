package com.devops.employee.exception;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestControllerAdvice
public class GlobalExceptionHandler {
 @ExceptionHandler(MethodArgumentNotValidException.class)
 public ProblemDetail validation(MethodArgumentNotValidException e){
  String msg=e.getBindingResult().getFieldErrors().stream().map(x->x.getField()+": "+x.getDefaultMessage()).findFirst().orElse("Invalid request");
  return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,msg);
 }
 @ExceptionHandler(ResponseStatusException.class)
 public ProblemDetail status(ResponseStatusException e){return ProblemDetail.forStatusAndDetail(e.getStatusCode(),e.getReason());}
}
