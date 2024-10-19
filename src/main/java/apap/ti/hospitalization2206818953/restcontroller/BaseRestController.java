package apap.ti.hospitalization2206818953.restcontroller;

import java.util.Date;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.resource.NoResourceFoundException;

import apap.ti.hospitalization2206818953.restdto.response.BaseResponseDTO;

@RestControllerAdvice
public class BaseRestController {
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleNoSourceFoundException(final NoResourceFoundException ex) {
        var baseResponseDTO = new BaseResponseDTO<NoResourceFoundException>();
        baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
        baseResponseDTO.setMessage("PATH URI tidak ditemukan");
        baseResponseDTO.setTimestamp(new Date());

        ex.printStackTrace();

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(final HttpMessageNotReadableException ex) {
        var baseResponseDTO = new BaseResponseDTO<HttpMessageNotReadableException>();
        baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
        baseResponseDTO.setMessage(String.format("Terjadi eror saat membaca request body, Error: %s", ex.getMessage()));
        baseResponseDTO.setTimestamp(new Date());

        ex.printStackTrace();

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(final Exception ex) {
        var baseResponseDTO = new BaseResponseDTO<Exception>();
        baseResponseDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        baseResponseDTO.setMessage(String.format("Terjadi eror pada server, Error : %s", ex.getMessage()));
        baseResponseDTO.setTimestamp(new Date());

        ex.printStackTrace();

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
