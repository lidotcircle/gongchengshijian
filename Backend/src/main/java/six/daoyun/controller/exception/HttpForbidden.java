package six.daoyun.controller.exception;

import org.springframework.http.HttpStatus;

public class HttpForbidden extends HttpException {
	private static final long serialVersionUID = -522166369572580444L;

    public HttpForbidden(String reason) {
        super(reason);
    }

    public HttpForbidden() {
        super(HttpStatus.FORBIDDEN.name());
    }
}

