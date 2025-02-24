package com.example.demo.service;

import com.example.demo.model.Inventario;
import com.example.demo.model.Jugador;
import com.example.demo.model.Usuario;
import com.example.demo.repository.JugadorRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private JugadorRepository jugadorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Al registrar un nuevo usuario si es USER se añade un jugador asociado
    public void registrarUsuario(String username, String password, String rol) {
        // Crear el usuario
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setRol(rol);

        if ("USER".equals(rol)) {
            // Crear el jugador asociado solo para usuarios con rol USER
            Jugador jugador = new Jugador();
            jugador.setNombre(username); // El nombre del jugador es el mismo que el nombre de usuario
            jugador.setNivel(1); // Nivel inicial
            jugador.setInventario(new Inventario()); // Inventario vacío (o con objetos por defecto)

            // Establecer la relación entre el usuario y el jugador
            usuario.setJugador(jugador);
            jugador.setUsuario(usuario);
        }

        // Guardar el usuario (y el jugador debido a la relación en cascada)
        usuarioRepository.save(usuario);
    }
}