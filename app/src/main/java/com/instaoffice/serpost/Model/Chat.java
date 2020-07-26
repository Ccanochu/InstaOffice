package com.instaoffice.serpost.Model;

public class Chat {

    private String emisor;
    private String receptor;
    private String mensaje;
    private boolean visto;
    private String fecham;

    public Chat(String emisor, String receptor, String mensaje, boolean visto, String fecham) {
        this.emisor = emisor;
        this.receptor = receptor;
        this.mensaje = mensaje;
        this.visto = visto;
        this.fecham = fecham;
    }

    public Chat() {
    }

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public String getReceptor() {
        return receptor;
    }

    public void setReceptor(String receptor) {
        this.receptor = receptor;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isVisto() {
        return visto;
    }

    public void setVisto(boolean visto) {
        this.visto = visto;
    }

    public String getFecham() {
        return fecham;
    }

    public void setFecham(String fecham) {
        this.fecham = fecham;
    }
}
