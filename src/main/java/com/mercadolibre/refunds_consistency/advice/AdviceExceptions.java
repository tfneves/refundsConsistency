package com.mercadolibre.refunds_consistency.advice;

import com.mercadolibre.refunds_consistency.constants.ConnectionConstants;
import com.mercadolibre.refunds_consistency.dto.RequestResponse;
import com.mercadolibre.refunds_consistency.exceptions.ConnectionTimeoutException;
import com.mercadolibre.refunds_consistency.exceptions.ForbiddenException;
import com.mercadolibre.refunds_consistency.exceptions.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ServerErrorException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AdviceExceptions {

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    private Map<String, String> httpMethodNotAllowed(HttpRequestMethodNotSupportedException e){
        Map<String, String> errors = new HashMap<>();
        errors.put("error_message", e.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    private Map<String, String> jsonFormatterException(HttpMessageNotReadableException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error_message", "Invalid JSON");
        errors.put("exception_message", e.getMessage());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException.class)
    private Map<String, String> missingServletRequestParameterException(MissingServletRequestParameterException e){
        Map<String, String> response = new HashMap<>();
        response.put("error", e.getMessage());
        return response;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ServerErrorException.class)
    private Map<String, String> serverErrorException(ServerErrorException e)
    {
        Map<String, String> response = new HashMap<>();
        response.put("error", e.getMessage());
        return response;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    private Map<String, String> httpClientErrorExceptionUnauthorized(HttpClientErrorException.Unauthorized e){
        Map<String, String> response = new HashMap<>();
        response.put("response", "Unauthorized access - Check your access credentials");
        response.put("error_message", e.getMessage());
        RequestResponse.statusResponse = null;
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingRequestHeaderException.class)
    private Map<String, String> missingRequestHeaderException(MissingRequestHeaderException e){
        Map<String, String> response = new HashMap<>();
        response.put("response", "Failed to get required headers");
        response.put("error_message", e.getMessage());
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private Map<String, String> argumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = ((FieldError) error).getDefaultMessage();
            errors.put("error_message", errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    @ExceptionHandler(UnauthorizedException.class)
    private Map<String, String> unauthorizedException(UnauthorizedException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("response", "Unauthorized access - Check your access credentials");
        errors.put("error_message", e.getMessage());
        RequestResponse.statusResponse = null;
        return errors;
    }

    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    @ResponseBody
    @ExceptionHandler(ConnectionTimeoutException.class)
    private Map<String, String> timeoutConnectionException(ConnectionTimeoutException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("response", String.format("Connection timeout %d milliseconds exceeded", ConnectionConstants.TIMEOUT_REQUEST));
        errors.put("error_message", e.getMessage());
        RequestResponse.statusResponse = null;
        return errors;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    @ExceptionHandler(ForbiddenException.class)
    private Map<String, String> forbiddenAPIAccess(ForbiddenException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("response", String.format("The user does not have an associated access group, for the application payins-api. You can request the required access-group at the following link https://mordor.adminml.com/access/traffic-catalog?access_groups=Payments-All-Corebackend-BankingConnects"));
        errors.put("error_message", e.getMessage());
        RequestResponse.statusResponse = null;
        return errors;
    }
}
