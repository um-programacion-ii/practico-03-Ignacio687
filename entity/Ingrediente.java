package entity;

import entity.customExceptions.StockInsuficienteException;

public class Ingrediente implements Cocinable {
    private String nombre;
    private Integer cantidad;

    public Ingrediente(String nombre, Integer cantidad) {
        this.nombre = nombre;
        this.cantidad = cantidad;
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
    public Integer getCantidad() {
        return cantidad;
    }

    @Override
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return nombre + "  Cantidad: "+cantidad;
    }

    @Override
    public void sacar(int amount) throws StockInsuficienteException {
        if (amount <= this.cantidad) {
            this.cantidad -= amount;
        } else {
            throw new StockInsuficienteException("Not enough remaining units of "+this.nombre+", available: "+this.cantidad);
        }
    }

    @Override
    public void restock(int amount) {
        this.cantidad += amount;
    }

}