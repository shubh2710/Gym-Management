package models;

public class admin {

	private String aid;
	private String name;
	private String email;
	private String password;
	private String username;
	private String profile;
	private int type;
	

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public admin(String aid, String name, String email, String password, String username, String profile,int type) {
		this.aid = aid;
		this.name = name;
		this.email = email;
		this.password = password;
		this.username = username;
		this.profile = profile;
		this.type=type;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public admin() {

	}

	public String getAid() {
		return aid;
	}

	public void setAid(String aid) {
		this.aid = aid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
