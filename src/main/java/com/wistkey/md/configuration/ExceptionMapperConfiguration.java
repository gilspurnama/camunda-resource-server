package com.wistkey.md.configuration;

import com.wistkey.md.dto.ResponseDto;
import com.wistkey.md.exception.ResponseException;
import com.wistkey.md.model.ErrorLog;
import com.wistkey.md.repository.ErrorLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;

@Slf4j
@RestControllerAdvice
public class ExceptionMapperConfiguration {

    private final ErrorLogRepository errorLogRepository;

    public ExceptionMapperConfiguration(ErrorLogRepository errorLogRepository) {
        this.errorLogRepository = errorLogRepository;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<?>> handler(Exception exception, HttpServletRequest httpServletRequest) throws IOException {
        log.error("ExceptionHandler",exception);
        if(exception instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) exception;
            return ResponseEntity.badRequest().body(new ResponseDto<>(null, methodArgumentNotValidException.getFieldError().getDefaultMessage()));
        }
        else if(exception instanceof ResponseException){
            ResponseException responseException = (ResponseException) exception;
            ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status(responseException.getStatus());
            if(responseException.getMessage() == null){
                return bodyBuilder.build();
            }

            ErrorLog errorLog = new ErrorLog();
            Map<String, String> headers = Collections.list(httpServletRequest.getHeaderNames())
                    .stream()
                    .collect(Collectors.toMap(h -> h, httpServletRequest::getHeader));

            InputStream inputStream = httpServletRequest.getInputStream();
            String text = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            errorLog.setRequestBody(text);

            inputStream.read();
            errorLog.setErrorCode(String.valueOf(responseException.getErrorCode()));
            errorLog.setMessage(responseException.getMessage());
            errorLog.setActivityName(String.valueOf(((ResponseException) exception).getStatus().series()));
            errorLog.setType(((ResponseException) exception).getStatus().name());
//            errorLog.setRequestBody(httpServletRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator())).replaceAll("\\s",""));
//            errorLog.setRequestBody(requestBody.replaceAll("\\s",""));
            errorLog.setRequestMethod(httpServletRequest.getMethod());
            errorLog.setRequestPath(httpServletRequest.getRequestURI());
            errorLog.setRequestHeader(headers.toString().replaceAll("\\s",""));
            errorLog.setCreatedAt(LocalDateTime.now(UTC));

            errorLogRepository.saveAndFlush(errorLog);
            return bodyBuilder.body(new ResponseDto<>(responseException.getMessage(), responseException.getErrorCode()));
        }
        else if(exception instanceof AccessDeniedException){
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new ResponseDto<>(exception.getMessage()));
        }
        else{
            ResponseEntity.BodyBuilder bodyBuilder = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
            ResponseDto<?> responseDto = new ResponseDto<>(exception.getMessage());
            return bodyBuilder.body(responseDto);
        }
    }

}
