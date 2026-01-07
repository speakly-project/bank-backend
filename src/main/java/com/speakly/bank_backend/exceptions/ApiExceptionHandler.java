package com.speakly.bank_backend.exceptions;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({UnauthorizedException.class})
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ErrorMessage handleUnauthorizedException(UnauthorizedException ex) {
        return new ErrorMessage(ex);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({ForbiddenException.class})
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ErrorMessage handleForbiddenException(ForbiddenException ex) {
        return new ErrorMessage(ex);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ResourceNotFoundException.class})
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ErrorMessage handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ErrorMessage(ex);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ValidationException.class, IllegalArgumentException.class})
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ErrorMessage handleValidationException(Exception ex) {
        return new ErrorMessage(ex);
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ErrorMessage handleGeneralException(Exception exception) {
        return new ErrorMessage(exception);
    }
}