package entity.recetas;

import entity.Cocinable;
import entity.Reutilizable;

import java.util.Map;

public class Pizza extends Receta{

    public Pizza() {
        this.tiempoCoccion = 30;
        Object[][] ingredientes = {{"harina", 1}, {"agua", 1}, {"aceite", 1}, {"queso", 2}, {"tomate", 3}};
        this.setDefaultIngredientes(ingredientes);
        Object[][] utensilios = {{"fuente", 30}, {"rodillo", 5}, {"cuchillo", 2}, {"cuchara", 1},
                {"tabla de cortar", 5}};
        this.setDefaultUtensilios(utensilios);
        this.preparacion = "Extender la masa, agregar salsa y queso, añadir ingredientes, hornear por 30 minutos o " +
                "hasta dorar.";
    }

    public Pizza(Integer tiempoCoccion, Map<String, Cocinable> ingredientes,
                 Map<String, Reutilizable> utensilios, String preparacion) {
        this.tiempoCoccion = tiempoCoccion;
        this.ingredientes = ingredientes;
        this.utensilios = utensilios;
        this.preparacion = preparacion;
    }

}
