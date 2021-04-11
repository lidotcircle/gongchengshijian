package six.daoyun.controller.exception;

import org.springframework.http.HttpStatus;

public class HttpInternalServerError extends HttpException {
	private static final long serialVersionUID = 1582923256248723151L;

	public HttpInternalServerError(String reason) {
        super(reason);
    }

    public HttpInternalServerError() {
        super(HttpStatus.INTERNAL_SERVER_ERROR.name());
    }
}

