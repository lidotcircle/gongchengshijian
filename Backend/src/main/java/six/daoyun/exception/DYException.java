package six.daoyun.exception;


public class DYException extends RuntimeException {
	private static final long serialVersionUID = 1L;

    private String reason;
    public String getReason() {
        return this.reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }

    public DYException() {
    }

    public DYException(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return this.reason;
    }
}

