package com.instaoffice.serpost.Model;

public class Usuario {

    private String id;
    private String nombreUsuario;
    private String departamento;
    private String provincia;
    private String sede;
    private String imagenURL;
    private String estado;
    private String busqueda;


    public Usuario() {

    }

    public Usuario(String id, String nombreUsuario, String departamento, String provincia, String sede, String imagenURL, String estado, String busqueda) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.departamento = departamento;
        this.provincia = provincia;
        this.sede = sede;
        this.imagenURL = imagenURL;
        this.estado = estado;
        this.busqueda = busqueda;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public String getImagenURL() {
        return imagenURL;
    }

    public void setImagenURL(String imagenURL) {
        this.imagenURL = imagenURL;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
    }
}
