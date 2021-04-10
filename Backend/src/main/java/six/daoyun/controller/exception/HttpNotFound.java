package six.daoyun.controller.exception;

import org.springframework.http.HttpStatus;

public class HttpNotFound extends HttpException {
	private static final long serialVersionUID = 1L;

    public HttpNotFound(String reason) {
        super(reason);
    }

    public HttpNotFound() {
        super(HttpStatus.NOT_FOUND.name());
    }
}

