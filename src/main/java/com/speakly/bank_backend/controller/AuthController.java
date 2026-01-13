package com.speakly.bank_backend.controller;


import com.speakly.bank_backend.annotations.Authenticated;
import com.speakly.bank_backend.controller.request.LoginRequest;
import com.speakly.bank_backend.domain.dto.LoginUserDto;
import com.speakly.bank_backend.domain.model.Client;
import com.speakly.bank_backend.domain.service.AuthService;
import com.speakly.bank_backend.domain.service.ClientService;
import com.speakly.bank_backend.exceptions.DtoValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/speakly/auth")
public class AuthController {
    private final AuthService authService;
    private final ClientService userService;


    public AuthController(AuthService authService, ClientService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        DtoValidator.validate(loginRequest);
        Client user = userService.getByName(loginRequest.username());

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Client authenticatedUser = new Client(
                user.getId(),
                user.getUsername(),
                loginRequest.password(),
                user.getName(),
                user.getFirstSurname(),
                user.getSecondSurname(),
                user.getDni(),
                user.getApi_token(),
                user.getAccounts()
        );
        String token = authService.createTokenForUser(authenticatedUser);

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @Authenticated
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        authService.deleteToken(token);
        return new ResponseEntity<>("Logout successful", HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<LoginUserDto> getUserFromToken(@RequestHeader("Authorization") String token) {
        LoginUserDto loggedUser = authService.getUserFromToken(token.substring(7));
        return new ResponseEntity<>(loggedUser, HttpStatus.OK);
    }
}
