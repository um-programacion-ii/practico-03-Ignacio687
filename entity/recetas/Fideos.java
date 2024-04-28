package entity.recetas;

import entity.Cocinable;
import entity.Reutilizable;

import java.util.Map;

public class Fideos extends Receta{

    public Fideos() {
        this.tiempoCoccion = 10;
        Object[][] ingredientes = {{"fideos", 1}, {"agua", 1}, {"sal", 1}};
        this.setDefaultIngredientes(ingredientes);
        Object[][] utensilios = {{"olla", 10}, {"tenedor", 2}, {"escurridor", 3}};
        this.setDefaultUtensilios(utensilios);
        this.preparacion = "Hervir agua en una olla. Agregar sal al agua hirviendo. Añadir los fideos y cocinar " +
                "durante 10 minutos. Escurrir y servir caliente.";
    }

    public Fideos(Integer tiempoCoccion, Map<String, Cocinable> ingredientes,
                  Map<String, Reutilizable> utensilios, String preparacion) {
        this.tiempoCoccion = tiempoCoccion;
        this.ingredientes = ingredientes;
        this.utensilios = utensilios;
        this.preparacion = preparacion;
    }

}
