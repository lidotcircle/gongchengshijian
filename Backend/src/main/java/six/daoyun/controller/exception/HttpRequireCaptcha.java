package six.daoyun.controller.exception;

import six.daoyun.exception.DYException;

public class HttpRequireCaptcha extends DYException {
	private static final long serialVersionUID = -522166369572580444L;

    public HttpRequireCaptcha(String reason) {
        super(reason);
    }

    public HttpRequireCaptcha() {
        // IMPORTANT!! don't change
        super("require captcha");
    }
}

