package six.daoyun.controller.exception;

import org.springframework.http.HttpStatus;

import six.daoyun.exception.NotFound;

public class HttpNotFound extends NotFound {
	private static final long serialVersionUID = 1L;

    public HttpNotFound(String reason) {
        super(reason);
    }

    public HttpNotFound() {
        super(HttpStatus.NOT_FOUND.name());
    }
}

