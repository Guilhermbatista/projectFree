package com.demov1.Dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.demov1.Entities.User;


public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private String password;
    private String phone;
    private LocalDate birthDate;
    private List<String> roles = new ArrayList<>();
    
	public UserDTO(Long id, String name, String email,String password ,String phone, LocalDate birthDate) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.birthDate = birthDate;
	}
    
	public UserDTO(User entity) {
		id = entity.getId();
		name = entity.getName();
		email = entity.getEmail();
		password = entity.getPassword();
		phone = entity.getPhone();
		birthDate = entity.getBirthDate();
		for (GrantedAuthority role : entity.getAuthorities()) {
			roles.add(role.getAuthority());
		}
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}
	

	public String getPassword() {
		return password;
	}

	public String getPhone() {
		return phone;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public List<String> getRoles() {
		return roles;
	}
}
