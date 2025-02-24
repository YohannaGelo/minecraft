package com.example.demo.controller;

import com.example.demo.model.Inventario;
import com.example.demo.model.Jugador;
import com.example.demo.model.Usuario;
import com.example.demo.repository.JugadorRepository;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.UsuarioService;

import java.security.Principal;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class JugadorController {

	@Autowired
	private JugadorRepository jugadorRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private static final String[] OBJETOS_DISPONIBLES = { 
		    "Espada de diamante", "Espada de hierro", "Espada de madera", "Espada de piedra", "Espada de netherite",
		    "Manzana dorada", "Manzana", "Pan", "Trigo", "Zanahoria", "Patata", "Remolacha", "Pescado cocinado",
		    "Poción de curación", "Poción de fuerza", "Poción de velocidad", "Poción de regeneración",
		    "Arco", "Lingote de hierro", "Flechas", "Escudo", "Tridente",
		    "Bloques de construcción", "Madera de roble", "Madera de abeto", "Madera de abedul", "Piedra", "Ladrillos",
		    "Antorchas", "Lingote de oro", "Glowstone", "Farol", 
		    "Pico de netherite", "Pico de diamante", "Pico de hierro", "Pico de piedra", "Pico de madera",
		    "Hacha de netherite", "Hacha de diamante", "Hacha de hierro", "Hacha de piedra", "Hacha de madera",
		    "Pala de hierro", "Azada de diamante", "Cubo de agua", "Cubo de lava",
		    "Mena de hierro", "Mena de oro", "Mena de diamante", "Mena de netherite", "Carbón", "Redstone", 
		    "Mesa de trabajo", "Horno", "Yunque", "Cartel", "Cofre", "Ender Chest", "Cama", "Rieles", "Vagoneta",
		    "Pólvora", "TNT", "Perla de Ender", "Ojo de Ender"
		};

	// Devuelve la vista login.html
	@GetMapping({ "/login", "/" })
	public String mostrarLogin() {
		return "login";
	}

	@GetMapping("/cerrarSesion")
	public String cerrarSesion() {
		return "redirect:/login?logout";
	}

	@GetMapping("/registro")
	public String mostrarRegistro() {
		return "registro";
	}

	// Crea el nuevo registro de usuario y jugador en la BBDD
	@PostMapping("/procesarRegistro")
	public String procesarRegistro(@RequestParam String username, @RequestParam String password) {
		usuarioService.registrarUsuario(username, password, "USER"); // Rol por defecto: USER
		return "redirect:/login"; // Redirigir al login después del registro
	}

	// Ruta principal para usuarios
	@GetMapping("/home")
	public String home(Model model, Principal principal) {
		String username = principal.getName();
		Usuario usuario = usuarioRepository.findByUsername(username)
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

		// Verificar si el usuario es ADMIN
		if ("ADMIN".equals(usuario.getRol())) {
			return "redirect:/jugadores"; // Redirigir a /jugadores si es ADMIN
		}

		// Verificar si el usuario tiene un jugador asociado
		if (usuario.getJugador() == null) {
			throw new RuntimeException("No se encontró un jugador asociado para el usuario: " + username);
		}

		// Pasar los datos a la vista
		model.addAttribute("usuario", usuario);
		model.addAttribute("jugador", usuario.getJugador());
		model.addAttribute("inventario", usuario.getJugador().getInventario());

		return "home";
	}

	// Ruta a zona para mostrar crafteos
	@GetMapping("/crafteos")
	public String crafteos() {
		return "crafteos";
	}

	// Mostrar la lista de jugadores - para ADMIN
	@GetMapping({ "/jugadores", "/" })
	public String listarJugadores(Model modelo) {
		modelo.addAttribute("jugadores", jugadorRepository.findAll());
		return "jugadores"; // Retorna la vista "jugadores.html"
	}

	// Mostrar el formulario para crear un nuevo jugador
	@GetMapping("/jugadores/crear")
	public String formularioCrear(Model modelo) {
		Jugador jugador = new Jugador();
		modelo.addAttribute("jugador", jugador);
		return "crear"; // Retorna la vista "crear.html"
	}

	@PostMapping("/jugadores")
	public String guardarJugador(@ModelAttribute("jugador") Jugador jugador, @RequestParam String items) {
		// Crear un nuevo inventario
		Inventario inventario = new Inventario();
		inventario.setItems(items); // Asignar los items del formulario al inventario

		// Asociar el inventario al jugador
		jugador.setInventario(inventario);

		// Crear un usuario asociado al jugador
		Usuario usuario = new Usuario();
		usuario.setUsername(jugador.getNombre()); // El nombre de usuario es el mismo que el nombre del jugador
		usuario.setPassword(passwordEncoder.encode(jugador.getNombre())); // Contraseña igual al nombre de usuario
		usuario.setRol("USER"); // Rol por defecto: USER

		// Establecer la relación entre el usuario y el jugador
		usuario.setJugador(jugador);
		jugador.setUsuario(usuario);

		// Guardar el usuario (y el jugador debido a la relación en cascada)
		usuarioRepository.save(usuario);

		return "redirect:/jugadores"; // Redirige a la lista de jugadores
	}

	// Mostrar el formulario para editar un jugador existente
	@GetMapping("/jugadores/editar/{id}")
	public String formularioEditar(@PathVariable Long id, Model modelo) {
		Jugador jugador = jugadorRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Jugador no encontrado"));
		modelo.addAttribute("jugador", jugador);
		return "editar"; // Retorna la vista "editar.html"
	}

	// Actualizar un jugador existente
	@PostMapping("/jugadores/{id}")
	public String actualizarJugador(@PathVariable Long id, @ModelAttribute("jugador") Jugador jugador,
			@RequestParam String items) {
		// Obtener el jugador existente
		Jugador jugadorExistente = jugadorRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Jugador no encontrado"));

		// Actualizar los campos del jugador
		jugadorExistente.setNombre(jugador.getNombre());
		jugadorExistente.setNivel(jugador.getNivel());

		// Actualizar los items del inventario
		jugadorExistente.getInventario().setItems(items);

		// Guardar el jugador (y el inventario debido a la relación en cascada)
		jugadorRepository.save(jugadorExistente);

		return "redirect:/jugadores"; // Redirige a la lista de jugadores
	}

	// Eliminar un jugador
	@GetMapping("/jugadores/{id}")
	public String eliminarJugador(@PathVariable Long id) {
		// Obtener el jugador por su ID
		Jugador jugador = jugadorRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Jugador no encontrado"));

		// Obtener el usuario asociado al jugador
		Usuario usuario = jugador.getUsuario();

		// Eliminar el jugador (y el usuario debido a la relación en cascada)
		jugadorRepository.delete(jugador);

		// Eliminar el usuario (si existe)
		if (usuario != null) {
			usuarioRepository.delete(usuario);
		}

		return "redirect:/jugadores"; // Redirige a la lista de jugadores
	}

	// Mostrar detalles de un jugador
	@GetMapping("/jugadores/detalles/{id}")
	public String detallesJugador(@PathVariable Long id, Model modelo) {
		Jugador jugador = jugadorRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Jugador no encontrado"));
		modelo.addAttribute("jugador", jugador);
		return "detalles"; // Retorna la vista "detalles.html"
	}
	
	@GetMapping("/obtener-objetos")
	public String obtenerObjetos(Principal principal, Model model) {
	    // Obtener el usuario actual
	    String username = principal.getName();
	    Usuario usuario = usuarioRepository.findByUsername(username)
	            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

	    // Obtener el jugador asociado al usuario
	    Jugador jugador = usuario.getJugador();
	    if (jugador == null) {
	        throw new RuntimeException("No se encontró un jugador asociado para el usuario: " + username);
	    }

	    // Obtener el inventario del jugador
	    Inventario inventario = jugador.getInventario();
	    if (inventario == null) {
	        throw new RuntimeException("No se encontró un inventario asociado al jugador");
	    }

	    // Verificar la capacidad del inventario
	    int capacidad = inventario.getCapacidad();
	    int cantidadActual = inventario.getItems().split(", ").length;

	    // Generar entre 2 y 5 objetos aleatorios
	    Random random = new Random();
	    int cantidadObjetos = random.nextInt(4) + 2; // Entre 2 y 5 objetos

	    // Añadir los nuevos objetos al inventario
	    StringBuilder nuevosItems = new StringBuilder(inventario.getItems());
	    for (int i = 0; i < cantidadObjetos; i++) {
	        String objeto = OBJETOS_DISPONIBLES[random.nextInt(OBJETOS_DISPONIBLES.length)];
	        if (nuevosItems.length() > 0) {
	            nuevosItems.append(", ");
	        }
	        nuevosItems.append(objeto);
	    }

	    // Actualizar el inventario
	    inventario.setItems(nuevosItems.toString());
	    jugadorRepository.save(jugador);

	    // Redirigir a la página de inicio
	    return "redirect:/home";
	}
}