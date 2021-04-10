package six.daoyun.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HttpExceptionAdvice {
    @ExceptionHandler(HttpBadRequest.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Object processBadRequest(HttpException ex) {
        return this.processHttpException(ex);
    }

    @ExceptionHandler(HttpUnauthorized.class)
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Object processUnauthorized(HttpException ex) {
        return this.processHttpException(ex);
    }

    @ExceptionHandler(HttpForbidden.class)
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    @ResponseBody
    public Object processForbidden(HttpException ex) {
        return this.processHttpException(ex);
    }

    @ExceptionHandler(HttpNotFound.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ResponseBody
    public Object processNotFound(HttpException ex) {
        return this.processHttpException(ex);
    }


    public Object processHttpException(HttpException ex) {
        HttpExceptionResponse ans = new HttpExceptionResponse();

        ans.setReason(ex.getReason());
        return ans;
    }
}

