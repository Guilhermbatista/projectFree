package com.demov1.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demov1.Entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	
	Optional<Role> findByAuthority(String authority);

}
