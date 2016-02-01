package ch.dietrich.database;

public class SuccessMessage {

	private boolean success;
	
	private String message;

	public SuccessMessage() {
		super();
		this.success = false;
		this.message = "";
	}

	public SuccessMessage(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
