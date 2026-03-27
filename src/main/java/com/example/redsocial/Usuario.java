package com.example.redsocial;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase que representa a un usuario en la red social.
 */
public class Usuario {
    private String nombre;
    private List<String> amigos;

    /**
     * Constructor para crear un usuario con un nombre.
     */
    public Usuario(String nombre) {
        this.nombre = nombre != null ? nombre.trim() : "";
        this.amigos = new ArrayList<>();
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
        if (otro == null)
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
     * ✅ MEJORADO: Sugiere amigos (amigos de amigos)
     */
    public List<String> sugerirAmigos(List<Usuario> todosLosUsuarios) {
        if (todosLosUsuarios == null)
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
     * Muestra la red de amigos (para depuración).
     */
    public void mostrarRed() {
        System.out.println("👤 Usuario: " + nombre);
        System.out.println("❤️ Amigos (" + amigos.size() + "): " + amigos);
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre != null ? nombre.trim() : "";
    }

    /**
     * ✅ Getter ESPECIAL para la vista - Devuelve lista INMUTABLE
     * Compatible con ${u.getAmigos().size()} y ${u.amigos} en Thymeleaf
     */
    public List<String> getAmigos() {
        return Collections.unmodifiableList(amigos != null ? amigos : new ArrayList<>());
    }

    /**
     * ✅ Getter directo para badges en HTML
     */
    public int getNumeroAmigos() {
        return amigos != null ? amigos.size() : 0;
    }

    /**
     * ✅ Para compatibilidad con vistas antiguas
     */
    public List<String> getAmigosLista() {
        return new ArrayList<>(amigos != null ? amigos : new ArrayList<>());
    }

    // Setter privado (para uso interno del controlador)
    public void setAmigos(List<String> amigos) {
        this.amigos = amigos != null ? new ArrayList<>(amigos) : new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Usuario usuario = (Usuario) o;
        return nombre.equalsIgnoreCase(usuario.nombre);
    }

    @Override
    public int hashCode() {
        return nombre.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return "Usuario{" + "nombre='" + nombre + '\'' + ", amigos=" + amigos.size() + '}';
    }
}