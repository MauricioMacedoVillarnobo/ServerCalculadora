package pucsp.ppr.st;

public class QuestoesExcetion extends Exception {

	public QuestoesExcetion() {
	}

	public QuestoesExcetion(String message) {
		super(message);
	}

	public QuestoesExcetion(Throwable cause) {
		super(cause);
	}

	public QuestoesExcetion(String message, Throwable cause) {
		super(message, cause);
	}

	public QuestoesExcetion(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
