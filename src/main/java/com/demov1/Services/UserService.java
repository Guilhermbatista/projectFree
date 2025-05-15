package com.demov1.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.demov1.Entities.Role;
import com.demov1.Entities.User;
import com.demov1.Projections.UserDetailsProjection;
import com.demov1.Repositories.UserRepository;
import com.demov1.Utils.CustomUserUtil;



@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private CustomUserUtil userUtil;

	public User authenticated() {
		try {
			String username = userUtil.getLoggedUsername();
			return repository.findByUsername(username).get();
		}
		catch (Exception e) {
			throw new UsernameNotFoundException("Invalid user");
		}
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		List<UserDetailsProjection> result = repository.searchUserAndRolesByUsername(username);
		if (result.size() == 0) {
			throw new UsernameNotFoundException("Email not found");
		}
		
		User user = new User();
		user.setEmail(result.get(0).getUsername());
		user.setPassword(result.get(0).getPassword());
		for (UserDetailsProjection projection : result) {
			user.addRole(new Role(projection.getRoleId(), projection.getAuthority()));
		}
		
		return user;
	}
	
}
