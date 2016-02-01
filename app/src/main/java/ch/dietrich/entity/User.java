package ch.dietrich.entity;

public class User {

	private Integer id = null;
	private String username = "";;
	private boolean male = false;
	private String email = "";
	private String activationcode = "";
	private boolean adFreeVersion = false;
	private int worngPasswordCount = 0;
	private String apiKey = "";

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isMale() {
		return male;
	}

	public void setMale(boolean male) {
		this.male = male;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getActivationcode() {
		return activationcode;
	}

	public void setActivationcode(String activationcode) {
		this.activationcode = activationcode;
	}

	public boolean isAdFreeVersion() {
		return adFreeVersion;
	}

	public void setAdFreeVersion(boolean adFreeVersion) {
		this.adFreeVersion = adFreeVersion;
	}

	public int getWorngPasswordCount() {
		return worngPasswordCount;
	}

	public void setWorngPasswordCount(int worngPasswordCount) {
		this.worngPasswordCount = worngPasswordCount;
	}

}
