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
     * ✅ PÁGINA PRINCIPAL - AGREGADO "contactos" para compatibilidad
     */
    @GetMapping("/")
    public String index(Model model) {
        // ✅ NUEVO: Alias "contactos" para la nueva tabla HTML
        model.addAttribute("contactos", usuarios);
        // ✅ Mantiene el nombre original para formularios
        model.addAttribute("usuarios", usuarios);
        return "index";
    }

    /**
     * ✅ Listar usuarios - AGREGADO "contactos"
     */
    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("contactos", usuarios);
        model.addAttribute("usuarios", usuarios);
        return "usuarios";
    }

    /**
     * Agregar amigo - CORREGIDO redirect
     */
    @PostMapping("/agregarAmigo")
    public String agregarAmigo(@RequestParam String usuario, @RequestParam String amigo,
            RedirectAttributes redirectAttributes) {
        Usuario u = encontrarUsuario(usuario);
        if (u != null) {
            boolean agregado = u.agregarAmigo(amigo);
            if (agregado) {
                redirectAttributes.addFlashAttribute("mensaje", "✅ Amigo '" + amigo + "' agregado a " + usuario);
            } else {
                redirectAttributes.addFlashAttribute("mensaje",
                        "⚠️ No se pudo agregar (ya existe o es el mismo usuario)");
            }
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "❌ Usuario '" + usuario + "' no encontrado");
        }
        return "redirect:/";
    }

    /**
     * Eliminar amigo - CORREGIDO redirect
     */
    @PostMapping("/eliminarAmigo")
    public String eliminarAmigo(@RequestParam String usuario, @RequestParam String amigo,
            RedirectAttributes redirectAttributes) {
        Usuario u = encontrarUsuario(usuario);
        if (u != null) {
            boolean eliminado = u.eliminarAmigo(amigo);
            if (eliminado) {
                redirectAttributes.addFlashAttribute("mensaje", "✅ Amigo '" + amigo + "' eliminado de " + usuario);
            } else {
                redirectAttributes.addFlashAttribute("mensaje", "⚠️ Amigo no encontrado en la lista");
            }
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "❌ Usuario '" + usuario + "' no encontrado");
        }
        return "redirect:/";
    }

    /**
     * Ver amigos en común - CORREGIDO redirect
     */
    @PostMapping("/amigosEnComun")
    public String amigosEnComun(@RequestParam String usuario1, @RequestParam String usuario2, Model model,
            RedirectAttributes redirectAttributes) {
        Usuario u1 = encontrarUsuario(usuario1);
        Usuario u2 = encontrarUsuario(usuario2);

        if (u1 != null && u2 != null) {
            List<String> comunes = u1.amigosEnComun(u2);
            model.addAttribute("usuarios", usuarios);
            model.addAttribute("contactos", usuarios);
            model.addAttribute("comunes", comunes.isEmpty() ? "No hay amigos en común 😔"
                    : "Amigos en común (" + comunes.size() + "): " + String.join(", ", comunes));
            return "index";
        } else {
            redirectAttributes.addFlashAttribute("mensaje", "❌ Uno de los usuarios no existe");
            return "redirect:/";
        }
    }

    /**
     * Ver sugerencias de amigos - MEJORADO
     */
    @PostMapping("/sugerencias")
    public String sugerencias(@RequestParam String usuario, Model model, RedirectAttributes redirectAttributes) {
        Usuario user = encontrarUsuario(usuario);

        if (user == null) {
            redirectAttributes.addFlashAttribute("mensaje", "❌ Usuario '" + usuario + "' no encontrado");
            return "redirect:/";
        }

        // Crear lista de sugerencias (Amigos de mis amigos)
        ArrayList<String> sugerencias = new ArrayList<>();
        for (String nombreAmigo : user.getAmigos()) {
            Usuario amigoObj = encontrarUsuario(nombreAmigo);
            if (amigoObj != null) {
                sugerencias.addAll(amigoObj.getAmigos());
            }
        }

        // Limpiar lista
        sugerencias.removeAll(user.getAmigos());
        sugerencias.remove(user.getNombre());

        List<String> sugerenciasFinales = sugerencias.stream()
                .distinct()
                .filter(s -> encontrarUsuario(s) != null)
                .collect(Collectors.toList());

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("contactos", usuarios);

        if (sugerenciasFinales.isEmpty()) {
            model.addAttribute("sugerencias", "No hay sugerencias para " + usuario + " 😊");
        } else {
            model.addAttribute("sugerencias",
                    "Sugerencias para " + usuario + " (" + sugerenciasFinales.size() + "): " +
                            String.join(", ", sugerenciasFinales));
        }

        model.addAttribute("usuario", user.getNombre());
        return "index";
    }

    /**
     * Método auxiliar para encontrar un usuario por nombre.
     */
    private Usuario encontrarUsuario(String nombre) {
        return usuarios.stream()
                .filter(u -> u.getNombre().equalsIgnoreCase(nombre.trim()))
                .findFirst()
                .orElse(null);
    }
}