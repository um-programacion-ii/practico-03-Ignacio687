package entity.recetas;

import entity.Cocinable;
import entity.Ingrediente;
import entity.Reutilizable;

import java.util.Map;

public class HuevoDuro extends Receta{

    public HuevoDuro() {
        this.tiempoCoccion = 10;
        Object[][] ingredientes = {{"huevo", 1}, {"agua", 1}};
        this.setDefaultIngredientes(ingredientes);
        Object[][] utensilios = {{"olla", 10}, {"cuchara", 1}};
        this.setDefaultUtensilios(utensilios);
        this.preparacion = "Poner un huevo en agua hirviendo durante 10 minutos.";
    }

    public HuevoDuro(Integer tiempoCoccion, Map<String, Cocinable> ingredientes,
                     Map<String, Reutilizable> utensilios, String preparacion) {
        this.tiempoCoccion = tiempoCoccion;
        this.ingredientes = ingredientes;
        this.utensilios = utensilios;
        this.preparacion = preparacion;
    }

}
