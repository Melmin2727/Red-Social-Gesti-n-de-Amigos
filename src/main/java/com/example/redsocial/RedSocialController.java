package com.example.redsocial;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador para manejar las rutas de la red social.
 */
@Controller
public class RedSocialController {

    // Lista en memoria de usuarios
    private List<Usuario> usuarios = new ArrayList<>();

    // Constructor para agregar usuarios de ejemplo
    public RedSocialController() {
        // Usuarios de ejemplo
        Usuario usuario1 = new Usuario("Alice");
        usuario1.agregarAmigo("Bob");
        usuario1.agregarAmigo("Charlie");

        Usuario usuario2 = new Usuario("Bob");
        usuario2.agregarAmigo("Alice");
        usuario2.agregarAmigo("Diana");

        Usuario usuario3 = new Usuario("Charlie");
        usuario3.agregarAmigo("Alice");
        usuario3.agregarAmigo("Eve");

        Usuario usuario4 = new Usuario("Diana");
        usuario4.agregarAmigo("Bob");

        Usuario usuario5 = new Usuario("Eve");
        usuario5.agregarAmigo("Charlie");

        usuarios.add(usuario1);
        usuarios.add(usuario2);
        usuarios.add(usuario3);
        usuarios.add(usuario4);
        usuarios.add(usuario5);
    }

    /**
     * Crear nuevo usuario.
     */
    @PostMapping("/crearUsuario")
    public String crearUsuario(@RequestParam String nombre, RedirectAttributes redirectAttributes) {
        // Verificamos si ya existe para no duplicar
        boolean existe = usuarios.stream().anyMatch(u -> u.getNombre().equalsIgnoreCase(nombre));

        if (existe) {
            redirectAttributes.addFlashAttribute("mensaje", "Error: El usuario '" + nombre + "' ya existe.");
        } else {
            usuarios.add(new Usuario(nombre));
            redirectAttributes.addFlashAttribute("mensaje", "¡Usuario '" + nombre + "' registrado con éxito!");
        }

        return "redirect:/";
    }

    /**
     * Página principal.
     */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("usuarios", usuarios);
        return "index";
    }

    /**
     * Listar usuarios.
     */
    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarios);
        return "usuarios";
    }

    /**
     * Agregar amigo.
     */
    @PostMapping("/agregarAmigo")
    public String agregarAmigo(@RequestParam String usuario, @RequestParam String amigo, Model model) {
        Usuario u = encontrarUsuario(usuario);
        if (u != null) {
            u.agregarAmigo(amigo);
        }
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("mensaje", "Amigo agregado si era válido.");
        return "index";
    }

    /**
     * Eliminar amigo.
     */
    @PostMapping("/eliminarAmigo")
    public String eliminarAmigo(@RequestParam String usuario, @RequestParam String amigo, Model model) {
        Usuario u = encontrarUsuario(usuario);
        if (u != null) {
            u.eliminarAmigo(amigo);
        }
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("mensaje", "Amigo eliminado si existía.");
        return "index";
    }

    /**
     * Ver amigos en común.
     */
    @PostMapping("/amigosEnComun")
    public String amigosEnComun(@RequestParam String usuario1, @RequestParam String usuario2, Model model) {
        Usuario u1 = encontrarUsuario(usuario1);
        Usuario u2 = encontrarUsuario(usuario2);
        List<String> comunes = new ArrayList<>();
        if (u1 != null && u2 != null) {
            comunes = u1.amigosEnComun(u2);
        }
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("comunes", comunes);
        model.addAttribute("usuario1", usuario1);
        model.addAttribute("usuario2", usuario2);
        return "index";
    }

    /**
     * Ver sugerencias de amigos - LÓGICA "AMIGOS DE AMIGOS" ✅
     */
    @PostMapping("/sugerencias")
    public String sugerencias(@RequestParam String usuario, Model model) {
        // 1. Encontrar al usuario en nuestra "red"
        Usuario user = usuarios.stream()
                .filter(u -> u.getNombre().equalsIgnoreCase(usuario))
                .findFirst()
                .orElse(null);

        if (user == null) {
            model.addAttribute("usuarios", usuarios);
            model.addAttribute("mensaje", "Usuario no encontrado");
            model.addAttribute("usuario", usuario);
            return "index";
        }

        // 2. Crear lista de sugerencias (Amigos de mis amigos)
        ArrayList<String> sugerencias = new ArrayList<>();

        for (String nombreAmigo : user.getAmigos()) {
            // Buscamos el objeto Usuario del amigo
            Usuario amigoObj = usuarios.stream()
                    .filter(u -> u.getNombre().equalsIgnoreCase(nombreAmigo))
                    .findFirst()
                    .orElse(null);

            if (amigoObj != null) {
                // Agregamos todos los amigos de mi amigo a la lista de sugerencias
                sugerencias.addAll(amigoObj.getAmigos());
            }
        }

        // 3. Limpiar la lista (La parte avanzada)
        // Quitamos a los que ya son mis amigos
        sugerencias.removeAll(user.getAmigos());

        // Nos quitamos a nosotros mismos de las sugerencias
        sugerencias.remove(user.getNombre());

        // Eliminar duplicados
        List<String> sugerenciasFinales = sugerencias.stream()
                .distinct()
                .filter(s -> encontrarUsuario(s) != null) // Solo usuarios que existen
                .collect(Collectors.toList());

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("sugerencias", sugerenciasFinales);
        model.addAttribute("usuario", user.getNombre());
        return "index";
    }

    /**
     * Método auxiliar para encontrar un usuario por nombre.
     */
    private Usuario encontrarUsuario(String nombre) {
        for (Usuario u : usuarios) {
            if (u.getNombre().equalsIgnoreCase(nombre)) {
                return u;
            }
        }
        return null;
    }
}