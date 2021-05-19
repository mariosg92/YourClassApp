package com.mariosg92.yourclassapp.clases;

import java.io.Serializable;
import java.util.Objects;

public class Clases implements Serializable {

    private String nombre;
    private String curso;

    public Clases(String nombre, String curso) {
        this.nombre = nombre;
        this.curso = curso;
    }

    public Clases() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Clases clases = (Clases) o;
        return Objects.equals(nombre, clases.nombre) &&
                Objects.equals(curso, clases.curso);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, curso);
    }

    @Override
    public String toString() {
        return "Clases{" +
                "nombre='" + nombre + '\'' +
                ", curso='" + curso + '\'' +
                '}';
    }
}
