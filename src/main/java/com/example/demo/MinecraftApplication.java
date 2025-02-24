package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class MinecraftApplication {

	public static void main(String[] args) {
		SpringApplication.run(MinecraftApplication.class, args);
		
		// Pequeño código para obtener las contraseñas encriptadas que reconoce SpringSecurity
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = encoder.encode("Pepe");
        System.out.println(password);
	}

}
