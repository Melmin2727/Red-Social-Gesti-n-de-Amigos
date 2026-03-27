package com.example.redsocial;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase que representa a un usuario en la red social.
 * ✅ 100% Null-safe y lista siempre inicializada
 */
public class Usuario {
    private String nombre;
    private List<String> amigos = new ArrayList<>(); // ✅ SIEMPRE inicializada

    /**
     * Constructor para crear un usuario con un nombre.
     */
    public Usuario(String nombre) {
        this.nombre = nombre != null ? nombre.trim() : "";
        // ✅ Lista ya inicializada arriba, doble protección
        if (this.amigos == null) {
            this.amigos = new ArrayList<>();
        }
    }

    /**
     * Agrega un amigo a la lista, sin duplicados y no a sí mismo.
     */
    public boolean agregarAmigo(String nombreAmigo) {
        if (nombreAmigo == null || nombreAmigo.trim().isEmpty())
            return false;

        String amigoLimpio = nombreAmigo.trim();
        if (amigoLimpio.equalsIgnoreCase(this.nombre) || amigos.contains(amigoLimpio)) {
            return false;
        }
        return amigos.add(amigoLimpio);
    }

    /**
     * Elimina un amigo de la lista.
     */
    public boolean eliminarAmigo(String nombreAmigo) {
        if (nombreAmigo == null)
            return false;
        return amigos.remove(nombreAmigo.trim());
    }

    /**
     * Obtiene la lista de amigos en común con otro usuario.
     */
    public List<String> amigosEnComun(Usuario otro) {
        if (otro == null || otro.amigos == null)
            return new ArrayList<>();

        List<String> comunes = new ArrayList<>();
        for (String amigo : amigos) {
            if (otro.amigos.contains(amigo)) {
                comunes.add(amigo);
            }
        }
        return comunes;
    }

    /**
     * Sugiere amigos basados en amigos de amigos.
     */
    public List<String> sugerirAmigos(List<Usuario> todosLosUsuarios) {
        if (todosLosUsuarios == null || todosLosUsuarios.isEmpty())
            return new ArrayList<>();

        List<String> sugerencias = new ArrayList<>();
        for (String amigo : amigos) {
            for (Usuario usuario : todosLosUsuarios) {
                if (usuario != null && usuario.nombre.equalsIgnoreCase(amigo)) {
                    for (String amigoDeAmigo : usuario.amigos) {
                        if (!amigoDeAmigo.equalsIgnoreCase(this.nombre) &&
                                !amigos.contains(amigoDeAmigo) &&
                                !sugerencias.contains(amigoDeAmigo)) {
                            sugerencias.add(amigoDeAmigo);
                        }
                    }
                }
            }
        }
        return sugerencias;
    }

    /**
     * Muestra la red de amigos (debug).
     */
    public void mostrarRed() {
        System.out.println("👤 " + nombre + " (" + amigos.size() + " amigos): " + amigos);
    }

    // ========== GETTERS ULTRA-SEGUROS ==========

    public String getNombre() {
        return nombre != null ? nombre : "";
    }

    public void setNombre(String nombre) {
        this.nombre = nombre != null ? nombre.trim() : "";
    }

    /**
     * ✅ PARA VISTAS HTML - SIEMPRE devuelve lista válida
     */
    public List<String> getAmigos() {
        return amigos != null ? Collections.unmodifiableList(amigos) : Collections.emptyList();
    }

    /**
     * ✅ PARA BADGES - SIEMPRE número válido
     */
    public int getNumeroAmigos() {
        return amigos != null ? amigos.size() : 0;
    }

    /**
     * ✅ Para iterar en Thymeleaf (copia editable)
     */
    public List<String> getAmigosLista() {
        return new ArrayList<>(getAmigos());
    }

    // Setter con protección
    public void setAmigos(List<String> amigos) {
        this.amigos = (amigos != null) ? new ArrayList<>(amigos) : new ArrayList<>();
    }

    // ========== OVERRIDES PARA ESTABILIDAD ==========

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Usuario usuario = (Usuario) o;
        return getNombre().equalsIgnoreCase(usuario.getNombre());
    }

    @Override
    public int hashCode() {
        return getNombre().toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return String.format("Usuario{nombre='%s', amigos=%d}", nombre, getNumeroAmigos());
    }
}