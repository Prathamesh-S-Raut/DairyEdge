package com.dairyedge.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dairyedge.backend.dtos.AuthRequest;
import com.dairyedge.backend.dtos.AuthResp;
import com.dairyedge.backend.dtos.UserDTO;
import com.dairyedge.backend.dtos.UserResponseDTO;
import com.dairyedge.backend.security.JwtUtils;
import com.dairyedge.backend.security.UserPrincipal;
import com.dairyedge.backend.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserDTO user){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.registerUser(user));
    }
    

    @PostMapping("/signin")
    public ResponseEntity<?> loginUser(@RequestBody @Valid AuthRequest request){
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        return ResponseEntity.ok(new AuthResp(jwtUtils.generateToken(principal), "Login Successful"));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId){
        return ResponseEntity.ok(userService.getUserDetails(userId));
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(){
        List<UserResponseDTO> users = userService.getAllUsers();
        if(users.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(users);
    }
    
    
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(
            @AuthenticationPrincipal UserPrincipal  principal) {

        Long userId = Long.parseLong(principal.getUserId());
        return ResponseEntity.ok(userService.getUserDetails(userId));
    }

}
