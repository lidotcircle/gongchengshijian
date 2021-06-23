package six.daoyun.controller.exception;

import org.springframework.http.HttpStatus;

import six.daoyun.exception.DYException;

public class HttpBadRequest extends DYException {
	private static final long serialVersionUID = 90831973211938486L;

    public HttpBadRequest(String reason) {
        super(reason);
    }

    public HttpBadRequest() {
        super(HttpStatus.BAD_REQUEST.name());
    }
}

