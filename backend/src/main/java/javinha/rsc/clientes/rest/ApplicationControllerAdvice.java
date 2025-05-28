package javinha.rsc.clientes.rest;

import javinha.rsc.clientes.rest.exception.ApiErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationErrors(MethodArgumentNotValidException ex){
       BindingResult bindingResult = ex.getBindingResult(); //Resultado da validação
       List<String> messages = bindingResult.getAllErrors()
               .stream()
               .map( objectError -> objectError.getDefaultMessage())
               .collect(Collectors.toList());

       return new ApiErrors(messages);
    }

//Metodo que permite qualquer tipo de retorno
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity handleResponseStatusExpection(ResponseStatusException ex){
        String mensagemErro = ex.getMessage();
        HttpStatus codigoStatus = (HttpStatus) ex.getStatusCode();
        ApiErrors apiErrors = new ApiErrors(mensagemErro);

        return new ResponseEntity(apiErrors, codigoStatus);
    }
}
