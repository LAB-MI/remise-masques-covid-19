package fr.gouv.interieur.dmgp.remettant.application.ui;

import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.security.InvalidParameterException;

@ControllerAdvice
public class DefaultExceptionHandler {
    private static Logger LOGGER = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handler404(NotFoundException e) {
        return "erreur-404";
    }

    @ExceptionHandler(value = InvalidParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handler400(InvalidParameterException e) {
        LOGGER.error("Paramètre utilisateur incorrecte", e);
        return "erreur-400";
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String defaultErrorHandler500(Exception e) {
        LOGGER.error("Erreur durant la distribution des masques", e);
        return "erreur-500";
    }
}
