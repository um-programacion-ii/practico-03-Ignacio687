package entity.recetas;

import entity.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class Receta {
    protected Integer tiempoCoccion;
    protected Map<String, Cocinable> ingredientes;
    protected Map<String, Reutilizable> utensilios;
    protected String preparacion;

    public Integer getTiempoCoccion() {
        return tiempoCoccion;
    }

    public void setTiempoCoccion(Integer tiempoCoccion) {
        this.tiempoCoccion = tiempoCoccion;
    }

    public Map<String, Cocinable> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(Map<String, Cocinable> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public Map<String, Reutilizable> getUtensilios() {
        return utensilios;
    }

    public void setUtensilios(Map<String, Reutilizable> utensilios) {
        this.utensilios = utensilios;
    }

    public String getPreparacion() {
        return preparacion;
    }

    public void setPreparacion(String preparacion) {
        this.preparacion = preparacion;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName()+", tiempoCoccion: " + tiempoCoccion +" m"+
                ", ingredientes:" + showItems(this.ingredientes) +
                ", \nutensilios: "+ showItems(this.utensilios)+
                ", \npreparacion: " + preparacion;
    }

    private static String showItems(Map<String, ? extends Despensable> itemsMap) {
        return itemsMap.values().stream()
                .map(Object::toString)
                .collect(Collectors.joining(", \n", "\n", ""));
    }

    protected void setDefaultIngredientes(Object[][] ingredientes) {
        this.ingredientes = Arrays.stream(ingredientes)
                .map(item -> new Ingrediente((String) item[0], (Integer) item[1]))
                .collect(Collectors.toMap(Ingrediente::getNombre, Function.identity()));
    }

    protected void setDefaultUtensilios(Object[][] utensilios) {
        this.utensilios = Arrays.stream(utensilios)
                .map(item -> new Utensilio((String) item[0], (Integer) item[1]))
                .collect(Collectors.toMap(Utensilio::getNombre, Function.identity()));
    }
}
