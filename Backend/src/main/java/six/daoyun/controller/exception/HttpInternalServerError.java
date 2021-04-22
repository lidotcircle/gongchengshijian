package six.daoyun.controller.exception;

import org.springframework.http.HttpStatus;

import six.daoyun.exception.DYException;

public class HttpInternalServerError extends DYException {
	private static final long serialVersionUID = 1582923256248723151L;

	public HttpInternalServerError(String reason) {
        super(reason);
    }

    public HttpInternalServerError() {
        super(HttpStatus.INTERNAL_SERVER_ERROR.name());
    }
}

