package entity;

import java.io.Serializable;
import java.sql.Timestamp;


@SuppressWarnings("serial")
public class AdminInfo implements Serializable {

	private Integer AdminId;
	private String AdminCode;
	private String Password;
	private String Name;
	private String Telephone;
	private String Email;
	private Timestamp Enroll;
	public Integer getAdminId() {
		return AdminId;
	}
	public void setAdminId(Integer adminId) {
		AdminId = adminId;
	}
	public String getAdminCode() {
		return AdminCode;
	}
	public void setAdminCode(String adminCode) {
		AdminCode = adminCode;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getTelephone() {
		return Telephone;
	}
	public void setTelephone(String telephone) {
		Telephone = telephone;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public Timestamp getEnroll() {
		return Enroll;
	}
	public void setEnroll(Timestamp enroll) {
		Enroll = enroll;
	}
	
	
}
