package com.demov1.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.demov1.Dto.UserDTO;
import com.demov1.Entities.Role;
import com.demov1.Entities.User;
import com.demov1.Repositories.RoleRepository;
import com.demov1.Repositories.UserRepository;
import com.demov1.Services.Exceptions.DatabaseException;
import com.demov1.Services.Exceptions.ForbiddenException;
import com.demov1.Services.Exceptions.ResourceNotFoundException;

@Service
public class AuthService {

	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository repository;
	@Autowired
	private RoleRepository roleRepository;

	public void validateSelfOrAdmin(Long userId) {
		User me = userService.authenticated();
		if (me.hasRole("ROLE_ADMIN")) {
			return;
		}
		if (!me.getId().equals(userId)) {
			throw new ForbiddenException("Access denied. Should be self or admin");
		}
	}

	@Transactional
	public UserDTO insert(UserDTO dto) {
		User user = new User();
		copyDtoToEntity(dto, user);
		user = repository.save(user);
		return new UserDTO(user);
	}
	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Falha de integridade referencial");

		}
	}

	private void copyDtoToEntity(UserDTO dto, User entity) {
		entity.setName(dto.getName());
		entity.setEmail(dto.getEmail());
		entity.setPassword(dto.getPassword());
		entity.setPhone(dto.getPhone());
		entity.setBirthDate(dto.getBirthDate());
		
		entity.getAuthorities().clear();
		for (String authority : dto.getRoles()) {
	        Role role = roleRepository.findByAuthority(authority)
	            .orElseThrow(() -> new ResourceNotFoundException ("Role not found: " + authority));
	        entity.addRole(role);
	    }
	}

}
