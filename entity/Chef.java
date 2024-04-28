package entity;

import entity.customExceptions.InvalidNameException;
import entity.customExceptions.MissingObjectException;
import entity.recetas.Receta;
import service.CocinaService;
import service.DespensaService;
import service.KitchenService;
import service.PantryService;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;

public class Chef {
    private String nombre;
    private Integer estrellasMichelin;
    private KitchenService kitchenService;
    private PantryService pantryService;

    public Chef() {
        this.nombre = "";
        this.estrellasMichelin = 0;
        this.kitchenService = CocinaService.getInstance();
        this.pantryService = new DespensaService();
    }

    public Chef(String nombre, Integer estrellasMichelin, KitchenService kitchenService, PantryService pantryService) {
        this.nombre = nombre;
        this.estrellasMichelin = estrellasMichelin;
        this.kitchenService = kitchenService;
        this.pantryService = pantryService;
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

    public PantryService getPantryService() {
        return pantryService;
    }

    public void setPantryService(PantryService pantryService) {
        this.pantryService = pantryService;
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
                String missingItems = this.kitchenService.getMissingItems(recetaName, this.pantryService);
                if (!Objects.equals(missingItems, "") && !runtimeExceptionFlag) {
                    System.out.println(missingItems);
                    System.out.println("\nRenovando el stock de la despensa...\n");
                }
                System.out.println("Preparando:  "+this.kitchenService.makeReceta(recetaName, this.pantryService));
                System.out.println(this.kitchenService.showPantryStatus(this.pantryService));
                break;
            } catch (MissingObjectException e) {
                if (runtimeExceptionFlag) {
                    throw new RuntimeException(e);
                }
                this.kitchenService.prepareKitchen(this.pantryService);
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

    public Runnable getCallableMakeReceta(String recetaName) {
        return new ChefRunnable(recetaName, this);
    }
}
