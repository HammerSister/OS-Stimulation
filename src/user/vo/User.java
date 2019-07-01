package user.vo;

public class User {
	private Boolean isAdmin;
	private  Integer userId;
    private String userName;
	private String userPassword;
	public Boolean getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(Boolean isAdmin){
		this.isAdmin = isAdmin;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserName(){
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPassword(){
		return userPassword;
	}
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	public String toString(){
		return "isAdmin"+isAdmin+";userId="+userId+";userName="+userName+";userPassword="+userPassword+";";
	}
}