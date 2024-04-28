package entity;

import entity.customExceptions.VidaUtilInsuficienteException;

public interface Reutilizable extends Despensable{

    public void use(int time) throws VidaUtilInsuficienteException;

    public void renew();

    public String getNombre();

    public void setNombre(String nombre);

    public Integer getVidaUtil();

    public void setVidaUtil(Integer vidaUtil);

    public Integer getvidaUtilInicial();

}
