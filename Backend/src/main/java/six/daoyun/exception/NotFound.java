package six.daoyun.exception;


public class NotFound extends DYException {
	private static final long serialVersionUID = 1;

    public NotFound(String reason) {
        super(reason);
    }

    public NotFound() {
        super("NotFound");
    }
}
