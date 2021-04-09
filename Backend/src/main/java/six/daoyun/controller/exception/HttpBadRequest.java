package six.daoyun.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Bad Request")
public class HttpBadRequest extends HttpException {
	private static final long serialVersionUID = 90831973211938486L;
}

