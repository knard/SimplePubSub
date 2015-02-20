package org.knard.simplePubSub;

public class PublishingException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6702215033320880170L;

	public PublishingException() {
		super();
	}

	public PublishingException(String message) {
		super(message);
	}

	public PublishingException(Throwable cause) {
		super(cause);
	}

	public PublishingException(String message, Throwable cause) {
		super(message, cause);
	}

}
