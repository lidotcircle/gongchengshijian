package six.daoyun.exception;


public class Unauthorized extends DYException {
	private static final long serialVersionUID = 1;

    public Unauthorized(String reason) {
        super(reason);
    }

    public Unauthorized() {
        super("Unauthorized");
    }
}
