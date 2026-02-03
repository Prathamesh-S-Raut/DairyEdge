package com.dairyedge.backend.service;

import java.util.List;

import com.dairyedge.backend.dtos.UserDTO;
import com.dairyedge.backend.dtos.UserResponseDTO;

public interface UserService {

    UserResponseDTO registerUser(UserDTO userDTO);

    UserResponseDTO getUserDetails(Long userId);

    List<UserResponseDTO> getAllUsers();
}
