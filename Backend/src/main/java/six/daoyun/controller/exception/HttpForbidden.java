package six.daoyun.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "forbidden")
public class HttpForbidden extends HttpException {
	private static final long serialVersionUID = -522166369572580444L;
}

