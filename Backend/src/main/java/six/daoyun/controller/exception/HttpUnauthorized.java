package six.daoyun.controller.exception;

import org.springframework.http.HttpStatus;

import six.daoyun.exception.Unauthorized;

public class HttpUnauthorized extends Unauthorized {
	private static final long serialVersionUID = 8686871360166522069L;

    public HttpUnauthorized(String reason) {
        super(reason);
    }

    public HttpUnauthorized() {
        super(HttpStatus.UNAUTHORIZED.name());
    }
}

