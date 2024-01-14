package com.bankingapplication.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.bankingapplication.dto.UserDTO;
import com.bankingapplication.entity.User;
import com.bankingapplication.exception.NotFoundException;
import com.bankingapplication.repo.UserRepository;
import com.bankingapplication.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final ModelMapper modelMapper;

	public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public List<UserDTO> getAllUsers() {
		List<User> users = userRepository.findAll();
		return users.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public UserDTO getUserById(Long id) throws NotFoundException {
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			return convertToDTO(user.get());
		} else {
			throw new NotFoundException("User not found.");
		}
	}

	@Override
	public UserDTO addUser(UserDTO userDTO) {
		User user = convertToEntity(userDTO);
		User savedUser = userRepository.save(user);
		return convertToDTO(savedUser);
	}

	@Override
	public UserDTO updateUser(Long id, UserDTO userDTO) throws NotFoundException {
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			User updatedUser = user.get();
			updatedUser.setName(userDTO.getName());
			updatedUser.setAccountNumber(userDTO.getAccountNumber());
			updatedUser.setAccountType(userDTO.getAccountType());
			// Set other fields accordingly
			updatedUser = userRepository.save(updatedUser);
			return convertToDTO(updatedUser);
		} else {
			throw new NotFoundException("User not found.");
		}
	}

	@Override
	public boolean deleteUser(Long id) throws NotFoundException {
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent()) {
			userRepository.delete(user.get());
			return true;
		} else {
			throw new NotFoundException("User not found.");
		}
	}

	private UserDTO convertToDTO(User user) {
		return modelMapper.map(user, UserDTO.class);
	}

	private User convertToEntity(UserDTO userDTO) {
		return modelMapper.map(userDTO, User.class);
	}

	public List<UserDTO> mapUsersToDTOs(List<User> users) {
		return users.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@Override
	public List<UserDTO> searchUsersByName(String name) {
		List<User> users = userRepository.findByNameContainingIgnoreCase(name);
		return mapUsersToDTOs(users);
	}
}
