package com.watch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotBlank(message = "Vui lòng điền username")
    private String username;
    @NotBlank(message = "Vui lòng điền password")
    @Size(min = 6,message = "Vui lòng điền từ 6 ký tự trở lên")
    private String password;
    @NotBlank(message = "Vui lòng điền lại password")
    private String rpassword;
    @NotBlank(message = "Vui lòng điền email")
    @Email(message = "Vui lòng điền đúng định dạng email")
    private String email;
    
    
    @NotBlank(message = "Vui lòng điền fullname")
    private String fullname;
    
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRpassword() {
		return rpassword;
	}
	public void setRpassword(String rpassword) {
		this.rpassword = rpassword;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
    
    

}