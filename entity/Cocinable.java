package entity;

import entity.customExceptions.StockInsuficienteException;

public interface Cocinable extends Despensable{

    public void sacar(int amount) throws StockInsuficienteException;

    public void restock(int amount);

    public String getNombre();

    public void setNombre(String nombre);

    public Integer getCantidad();

    public void setCantidad(Integer cantidad);
}
