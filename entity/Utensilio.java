package entity;

import entity.customExceptions.VidaUtilInsuficienteException;

public class Utensilio implements Reutilizable {
    private String nombre;
    private Integer vidaUtil;
    private Integer vidaUtilInicial;

    public Utensilio(String nombre, Integer vidaUtil) {
        this.nombre = nombre;
        this.vidaUtil = vidaUtil;
        this.vidaUtilInicial = vidaUtil;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public Integer getVidaUtil() {
        return vidaUtil;
    }

    @Override
    public void setVidaUtil(Integer vidaUtil) {
        this.vidaUtil = vidaUtil;
        this.vidaUtilInicial = vidaUtil;
    }

    @Override
    public Integer getvidaUtilInicial() {
        return this.vidaUtilInicial;
    }

    @Override
    public String toString() {
        return nombre + "  Vida útil: "+vidaUtil;
    }

    @Override
    public void use(int usageTime) throws VidaUtilInsuficienteException {
        if (usageTime <= this.vidaUtil) {
            this.vidaUtil -= usageTime;
        } else {
            throw new VidaUtilInsuficienteException(String.format("Not enough remaining usage time of %s", this.nombre));
        }
    }

    @Override
    public void renew() {
        this.vidaUtil = this.vidaUtilInicial;
    }
}
