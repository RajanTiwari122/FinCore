package com.bank.ib.exceptions;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.net.SocketTimeoutException;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeoutException;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 - Price not found
    @ExceptionHandler(PriceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handlePriceNotFound(PriceNotFoundException ex) {

        log.warn("Price not found", ex);

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Price Not Found");
        problem.setDetail(ex.getMessage());

        // optional cause
        if (ex.getCause() != null) {
            problem.setProperty("cause", ex.getCause().getMessage());
        }

        problem.setProperty("exception", ex.getClass().getSimpleName());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    // 500 - Null pointer (internal bug)
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ProblemDetail> handleNullPointerException(NullPointerException ex) {

        log.error("Null pointer exception", ex);

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setTitle("Internal Server Error");
        problem.setDetail("Unexpected system error occurred");

        // include cause only if present
        if (ex.getCause() != null) {
            problem.setProperty("cause", ex.getCause().getMessage());
        }

        problem.setProperty("exception", ex.getClass().getSimpleName());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);
    }

    // 400 - SQL syntax / bad query
    @ExceptionHandler(BadSqlGrammarException.class)
    public ResponseEntity<ProblemDetail> handleBadSqlGrammarException(BadSqlGrammarException ex) {

        log.error("Bad SQL grammar", ex);

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Invalid Database Query");
        problem.setDetail("Request could not be processed due to invalid query");

        // root SQL cause
        if (ex.getSQLException() != null) {
            problem.setProperty("cause", ex.getSQLException().getMessage());
        }

        problem.setProperty("exception", ex.getClass().getSimpleName());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ProblemDetail> handleRuntimeException(RuntimeException ex) {
        log.error("Unhandled RunTimeException", ex);
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setTitle("Runtime Error");
        problem.setDetail(ex.getMessage());

        // ✅ include root cause safely
        Throwable rootCause = ex.getCause();
        if (rootCause != null) {
            problem.setProperty("cause", rootCause.getMessage());
        }

        // optional metadata
        problem.setProperty("exception", ex.getClass().getSimpleName());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(problem);
    }
    // 400 - Bad Request
    @ExceptionHandler({
            IllegalArgumentException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ProblemDetail> handleBadRequest(Exception ex) {

        log.warn("Bad request", ex);

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Bad Request");
        problem.setDetail(ex.getMessage());
        problem.setProperty("exception", ex.getClass().getSimpleName());

        if (ex.getCause() != null) {
            problem.setProperty("cause", ex.getCause().getMessage());
        }

        return ResponseEntity.badRequest().body(problem);
    }

    // 404 - Not Found
    @ExceptionHandler({
            EntityNotFoundException.class,
            NoSuchElementException.class
    })
    public ResponseEntity<ProblemDetail> handleNotFound(Exception ex) {

        log.warn("Resource not found", ex);

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Resource Not Found");
        problem.setDetail(ex.getMessage());
        problem.setProperty("exception", ex.getClass().getSimpleName());

        if (ex.getCause() != null) {
            problem.setProperty("cause", ex.getCause().getMessage());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

    // 408 - Request Timeout
    @ExceptionHandler({
            TimeoutException.class,
            SocketTimeoutException.class
    })
    public ResponseEntity<ProblemDetail> handleTimeout(Exception ex) {

        log.error("Request timeout", ex);

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.REQUEST_TIMEOUT);
        problem.setTitle("Request Timeout");
        problem.setDetail("The request timed out while waiting for a response");
        problem.setProperty("exception", ex.getClass().getSimpleName());

        if (ex.getCause() != null) {
            problem.setProperty("cause", ex.getCause().getMessage());
        }

        return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(problem);
    }

    // 503 - External system / dependency failure
    @ExceptionHandler({
            RestClientException.class,
            HttpServerErrorException.class
    })
    public ResponseEntity<ProblemDetail> handleExternalFailure(Exception ex) {

        log.error("External service failure", ex);

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.SERVICE_UNAVAILABLE);
        problem.setTitle("Service Unavailable");
        problem.setDetail("External service temporarily unavailable");
        problem.setProperty("exception", ex.getClass().getSimpleName());

        if (ex.getCause() != null) {
            problem.setProperty("cause", ex.getCause().getMessage());
        }

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(problem);
    }

    // 500 - Fallback (must be LAST)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneric(Exception ex) {

        log.error("Unhandled exception", ex);

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problem.setTitle("Internal Server Error");
        problem.setDetail("Something went wrong. Please contact support.");
        problem.setProperty("exception", ex.getClass().getSimpleName());

        if (ex.getCause() != null) {
            problem.setProperty("cause", ex.getCause().getMessage());
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(problem);
    }

}

