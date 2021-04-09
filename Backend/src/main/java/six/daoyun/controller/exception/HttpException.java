package six.daoyun.controller.exception;

import java.lang.reflect.Field;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Internal Server Error")
public class HttpException extends RuntimeException {
	private static final long serialVersionUID = 1L;
    private int statusCode;
    private String statusText;

    private void annotationFetch() {
        for(Field field: HttpException.class.getFields()) {
            ResponseStatus status = field.getAnnotation(ResponseStatus.class);
            if(status != null) {
                this.statusCode = status.code().value();
                this.statusText = status.code().name();
                break;
            }
        }
    }

    public HttpException() {
        this.annotationFetch();
    }

    public HttpException(String reason) {
        this.annotationFetch();
        this.statusText = reason;
    }

    public String message() {
        return this.statusCode + this.statusText;
    }
}

