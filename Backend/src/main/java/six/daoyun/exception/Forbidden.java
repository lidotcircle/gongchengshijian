package six.daoyun.exception;


public class Forbidden extends DYException {
	private static final long serialVersionUID = 1;

	public Forbidden(String reason) {
        super(reason);
    }

    public Forbidden() {
        super("Forbidden");
    }
}

