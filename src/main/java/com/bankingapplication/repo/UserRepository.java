package com.bankingapplication.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bankingapplication.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	List<User> findByNameContainingIgnoreCase(String name);
}
