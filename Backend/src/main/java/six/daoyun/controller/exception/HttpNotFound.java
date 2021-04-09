package six.daoyun.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "not found")
public class HttpNotFound extends HttpException {
	private static final long serialVersionUID = 1L;
}

