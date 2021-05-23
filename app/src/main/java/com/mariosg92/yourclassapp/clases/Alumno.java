package com.mariosg92.yourclassapp.clases;

import java.io.Serializable;
import java.util.Objects;

public class Alumno implements Serializable {

    private String nombre;
    private String apellido1;
    private String apellido2;
    private long puntos;
    private String codigo;


    public Alumno(String nombre, String apellido1, String apellido2, String codigo) {
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.puntos = 0;
        this.codigo = codigo;
    }

    public Alumno(String nombre, String apellido1, String apellido2, long puntos) {
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.puntos = puntos;
    }
    public Alumno(){

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public long getPuntos() {
        return puntos;
    }

    public void setPuntos(long puntos) {
        this.puntos = puntos;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alumno alumno = (Alumno) o;
        return Objects.equals(nombre, alumno.nombre) &&
                Objects.equals(apellido1, alumno.apellido1) &&
                Objects.equals(apellido2, alumno.apellido2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, apellido1, apellido2);
    }

    @Override
    public String toString() {
        return "Alumno{" +
                "nombre='" + nombre + '\'' +
                ", apellido1='" + apellido1 + '\'' +
                ", apellido2='" + apellido2 + '\'' +
                ", puntos=" + puntos +
                '}';
    }
}
