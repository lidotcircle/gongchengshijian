package six.daoyun.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Internal Server Error")
public class HttpException extends RuntimeException {
	private static final long serialVersionUID = 1L;

    private String reason;
    public String getReason() {
        return this.reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }

    public HttpException() {
    }

    public HttpException(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return this.reason;
    }
}

