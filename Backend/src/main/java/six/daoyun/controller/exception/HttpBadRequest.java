package six.daoyun.controller.exception;

import org.springframework.http.HttpStatus;

public class HttpBadRequest extends HttpException {
	private static final long serialVersionUID = 90831973211938486L;

    public HttpBadRequest(String reason) {
        super(reason);
    }

    public HttpBadRequest() {
        super(HttpStatus.BAD_REQUEST.name());
    }
}

