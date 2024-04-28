package service;

import entity.customExceptions.*;
import entity.recetas.Receta;

import java.util.Map;
import java.util.stream.Collectors;

public interface KitchenService {

    Map<String, Receta> getRecetas();

    void setRecetas(Map<String, Receta> recetas);

    static String showRecetas(Map<String, Receta> recetas) {
        return "recetas: " + recetas.values().stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n\n- ", "\n\n- ", ""));
    }

    void addReceta(String recetaName, Receta receta) throws ObjectAlreadyExistsException;

    Receta getReceta(String recetaName) throws InvalidNameException;

    void restockKitchen(PantryService pantryService);

    void prepareKitchen(PantryService pantryService);

    String getMissingItems(String recetaName, PantryService pantryService) throws InvalidNameException;

    String makeReceta(String recetaName, PantryService pantryService) throws InvalidNameException, MissingObjectException;

    String showPantryStatus(PantryService pantryService);
}
