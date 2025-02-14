package com.mycar.business.controllers;


import com.mycar.business.controllers.dto.user.LoginDTO;
import com.mycar.business.security.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginDTO login, HttpServletResponse response) throws AuthenticationException {
        String email = login.getEmail();
        String password = login.getPassword();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtUtils.generateToken(userDetails);

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true); // Previene acceso desde JavaScript
        cookie.setSecure(true);  // Solo se envía por HTTPS (actívalo en producción)
        cookie.setPath("/");     // Disponible para toda la aplicación
        cookie.setMaxAge(2 * 60 * 60); // Expira en 2 horas
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        // Invalida la cookie configurando su expiración en el pasado
        Cookie jwtCookie = new Cookie("token", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true); // Usar true si tienes HTTPS
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0); // Esto hace que la cookie expire inmediatamente
        response.addCookie(jwtCookie);

        return ResponseEntity.ok("Logout successful");
    }
}

