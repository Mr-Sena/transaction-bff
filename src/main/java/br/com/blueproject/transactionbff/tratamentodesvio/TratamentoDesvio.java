package br.com.blueproject.transactionbff.tratamentodesvio;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE) // Ordem de precedência para executar os filtros.
// (Menor número da ordem, maior prioridade de precedência.)
public class TratamentoDesvio {

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> unauthorized (UnauthorizedException failure) {
        return response(failure.getMessage(), HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> internalServerError (Exception failure) {
        return response("Erro genérico.", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> NotFoundResource (NotFoundException failure) {
        return response(failure.getMessage(), HttpStatus.NOT_FOUND);
    }



    private ResponseEntity<ErrorResponse> response(String message, HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus).body(new ErrorResponse(message, httpStatus.value()) );
    }
}
