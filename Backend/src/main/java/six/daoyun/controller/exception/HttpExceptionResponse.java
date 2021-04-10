package six.daoyun.controller.exception;

import java.io.Serializable;

public class HttpExceptionResponse implements Serializable {
	private static final long serialVersionUID = -7802391609920591207L;

    private String reason;
    public String getReason() {
        return this.reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
}

