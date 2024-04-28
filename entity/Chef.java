package entity;

import entity.customExceptions.InvalidNameException;
import entity.customExceptions.MissingObjectException;
import entity.recetas.Receta;
import service.CocinaService;
import service.KitchenService;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Chef {
    private String nombre;
    private Integer estrellasMichelin;
    private KitchenService kitchenService;

    public Chef() {
        this.nombre = "";
        this.estrellasMichelin = 0;
        this.kitchenService = new CocinaService();
    }

    public Chef(String nombre, Integer estrellasMichelin, KitchenService kitchenService) {
        this.nombre = nombre;
        this.estrellasMichelin = estrellasMichelin;
        this.kitchenService = kitchenService;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getEstrellasMichelin() {
        return estrellasMichelin;
    }

    public void setEstrellasMichelin(Integer estrellasMichelin) {
        this.estrellasMichelin = estrellasMichelin;
    }

    public KitchenService getKitchenService() {
        return kitchenService;
    }

    public void setKitchenService(KitchenService kitchenService) {
        this.kitchenService = kitchenService;
    }

    @Override
    public String toString() {
        return "Chef " + nombre +
                ", Estrellas Michelin: " + estrellasMichelin;
    }

    public void showAvailableRecipes() {
        System.out.println(KitchenService.showRecetas(this.kitchenService.getRecetas()));
    }

    public Set<String> getRecipesNames() {
        Set<String> recipesNames = new HashSet<>();
        for (Map.Entry<String, Receta> receta: this.kitchenService.getRecetas().entrySet()) {
            recipesNames.add(receta.getKey());
        }
        return recipesNames;
    }

    public void makeReceta(String recetaName) throws InvalidNameException {
        boolean runtimeExceptionFlag = false;
        System.out.println("\nVerificando disponibilidad para preparar la receta: "
                +recetaName.trim().toLowerCase()+"\n");
        while (true) {
            try {
                String missingItems = this.kitchenService.getMissingItems(recetaName);
                if (!Objects.equals(missingItems, "") && !runtimeExceptionFlag) {
                    System.out.println(missingItems);
                    System.out.println("\nRenovando el stock de la despensa...\n");
                }
                System.out.println("Preparando:  "+this.kitchenService.makeReceta(recetaName));
                System.out.println(this.kitchenService.showPantryStatus());
                break;
            } catch (MissingObjectException e) {
                if (runtimeExceptionFlag) {
                    throw new RuntimeException(e);
                }
                this.kitchenService.prepareKitchen();
                runtimeExceptionFlag = true;
            }
        }
    }

    public void makeRecetas() {
        for (Map.Entry<String, Receta> receta: this.kitchenService.getRecetas().entrySet()) {
            boolean runtimeExceptionFlag = false;
            try {
                this.makeReceta(receta.getKey());
            } catch (InvalidNameException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
