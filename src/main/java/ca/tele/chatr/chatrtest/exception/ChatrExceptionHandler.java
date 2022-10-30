package ca.tele.chatr.chatrtest.exception;

import ca.tele.chatr.chatrtest.model.ErrorDto;
import ca.tele.chatr.chatrtest.model.ResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ChatrExceptionHandler extends ResponseEntityExceptionHandler {

    //handles globally 404 exception
    @ExceptionHandler(NoRecordsFoundException.class)
    public ResponseEntity<ResponseDto> handleNoRecordsFoundException(NoRecordsFoundException noRecordsFoundException){
        ErrorDto errorDto = new ErrorDto(HttpStatus.NOT_FOUND.value(), noRecordsFoundException.getMessage());

        return new ResponseEntity(ResponseDto.forFailure(Arrays.asList(errorDto)), HttpStatus.NOT_FOUND);
    }

    //handles globally 500 exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto> handleException(Exception exception){
        ErrorDto errorDto = new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR.value(),  exception.getMessage());

        return new ResponseEntity(ResponseDto.forFailure(Arrays.asList(errorDto)), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //handles globally 400 exception
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseDto> handleBadRequestException(BadRequestException exception){
        ErrorDto errorDto = new ErrorDto(HttpStatus.BAD_REQUEST.value(), exception.getMessage());

        return new ResponseEntity(ResponseDto.forFailure(Arrays.asList(errorDto)), HttpStatus.BAD_REQUEST);
    }

    //handles globally 422 exception
    @ExceptionHandler(UnHandeledEntityException.class)
    public ResponseEntity<ResponseDto> handleBadRequestException(UnHandeledEntityException exception){
        ErrorDto errorDto = new ErrorDto(HttpStatus.UNPROCESSABLE_ENTITY.value(),  exception.getMessage());

        return new ResponseEntity(ResponseDto.forFailure(Arrays.asList(errorDto)), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        Map<String, List<String>> body = new HashMap<>();

        List<ErrorDto> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> new ErrorDto(x.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ResponseEntity(ResponseDto.forFailure(errors), HttpStatus.BAD_REQUEST);
    }
}
