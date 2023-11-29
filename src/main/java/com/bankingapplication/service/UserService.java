package com.bankingapplication.service;

import java.util.List;

import com.bankingapplication.dto.UserDTO;
import com.bankingapplication.exception.NotFoundException;

public interface UserService {

	List<UserDTO> getAllUsers();

	UserDTO getUserById(Long id) throws NotFoundException;

	UserDTO addUser(UserDTO userDTO);

	UserDTO updateUser(Long id, UserDTO userDTO) throws NotFoundException;

	boolean deleteUser(Long id) throws NotFoundException;
}
