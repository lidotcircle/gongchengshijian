package six.daoyun.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "unauthorized")
public class HttpUnauthorized extends RuntimeException {
	private static final long serialVersionUID = 1L;
}

