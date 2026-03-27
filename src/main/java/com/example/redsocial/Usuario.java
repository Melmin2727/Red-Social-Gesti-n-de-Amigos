package com.example.redsocial;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa a un usuario en la red social.
 */
public class Usuario {
    private String nombre;
    private List<String> amigos;

    /**
     * Constructor para crear un usuario con un nombre.
     * 
     * @param nombre El nombre del usuario.
     */
    public Usuario(String nombre) {
        this.nombre = nombre;
        this.amigos = new ArrayList<>();
    }

    /**
     * Agrega un amigo a la lista, sin duplicados y no a sí mismo.
     * 
     * @param nombreAmigo El nombre del amigo a agregar.
     * @return true si se agregó, false si ya era amigo o es él mismo.
     */
    public boolean agregarAmigo(String nombreAmigo) {
        if (nombreAmigo.equals(this.nombre) || amigos.contains(nombreAmigo)) {
            return false;
        }
        amigos.add(nombreAmigo);
        return true;
    }

    /**
     * Elimina un amigo de la lista.
     * 
     * @param nombreAmigo El nombre del amigo a eliminar.
     * @return true si se eliminó, false si no estaba en la lista.
     */
    public boolean eliminarAmigo(String nombreAmigo) {
        return amigos.remove(nombreAmigo);
    }

    /**
     * Obtiene la lista de amigos en común con otro usuario.
     * 
     * @param otro El otro usuario.
     * @return Lista de amigos en común.
     */
    public List<String> amigosEnComun(Usuario otro) {
        List<String> comunes = new ArrayList<>();
        for (String amigo : amigos) {
            if (otro.amigos.contains(amigo)) {
                comunes.add(amigo);
            }
        }
        return comunes;
    }

    /**
     * Sugiere amigos basados en amigos de amigos que no estén en la lista.
     * Nota: Para esto, necesitamos acceso a otros usuarios. Este método asume que
     * se pasa una lista de todos los usuarios.
     * 
     * @param todosLosUsuarios Lista de todos los usuarios en la red.
     * @return Lista de sugerencias.
     */
    public List<String> sugerirAmigos(List<Usuario> todosLosUsuarios) {
        List<String> sugerencias = new ArrayList<>();
        for (String amigo : amigos) {
            for (Usuario usuario : todosLosUsuarios) {
                if (usuario.nombre.equals(amigo)) {
                    for (String amigoDeAmigo : usuario.amigos) {
                        if (!amigoDeAmigo.equals(this.nombre) && !amigos.contains(amigoDeAmigo)
                                && !sugerencias.contains(amigoDeAmigo)) {
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
        System.out.println("Usuario: " + nombre);
        System.out.println("Amigos: " + amigos);
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public List<String> getAmigos() {
        return amigos;
    }
}