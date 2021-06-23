package six.daoyun.controller.exception;

import java.io.Serializable;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import six.daoyun.exception.DYException;
import six.daoyun.exception.Forbidden;
import six.daoyun.exception.NotFound;
import six.daoyun.exception.Unauthorized;

@RestControllerAdvice
public class HttpExceptionAdvice {
    static class HttpExceptionResponse implements Serializable {
        private static final long serialVersionUID = -7802391609920591207L;

        private String reason;
        public String getReason() {
            return this.reason;
        }
        public void setReason(String reason) {
            this.reason = reason;
        }
    }

    @ExceptionHandler(HttpBadRequest.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Object processBadRequest(DYException ex) {
        return this.processDYException(ex);
    }

    @ExceptionHandler(Unauthorized.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public Object processUnauthorized(Unauthorized ex) {
        return this.processDYException(ex);
    }

    @ExceptionHandler(Forbidden.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public Object processForbidden(Forbidden ex) {
        return this.processDYException(ex);
    }

    @ExceptionHandler(NotFound.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public Object processNotFound(NotFound ex) {
        return this.processDYException(ex);
    }

    @ExceptionHandler(HttpRequireCaptcha.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public Object processRequireCaptcha(DYException ex) {
        return this.processDYException(ex);
    }

    @ExceptionHandler(DYException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public Object processInternalServerError(DYException ex) {
        return this.processDYException(ex);
    }

    public Object processDYException(DYException ex) {
        HttpExceptionResponse ans = new HttpExceptionResponse();

        ans.setReason(ex.getReason());
        return ans;
    }
}

