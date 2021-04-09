package six.daoyun.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "unauthorized")
public class HttpUnauthorized extends HttpException {
	private static final long serialVersionUID = 8686871360166522069L;
}

