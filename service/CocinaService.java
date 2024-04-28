package service;

import entity.*;
import entity.customExceptions.*;
import entity.recetas.Receta;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class CocinaService implements KitchenService{
    private Map<String, Receta> recetas;
    private static CocinaService instance;

    private CocinaService() {
        this.recetas = new HashMap<>();
    }

    @Override
    public Map<String, Receta> getRecetas() {
        return recetas;
    }

    @Override
    public void setRecetas(Map<String, Receta> recetas) {
        this.recetas = recetas;
    }

    @Override
    public String toString() {
        return "CocinaService, " + KitchenService.showRecetas(this.recetas);
    }

    public static KitchenService getInstance() {
        if (instance == null) {
            instance = new CocinaService();
        }
        return instance;
    }

    @Override
    public void addReceta(String recetaName, Receta receta) throws ObjectAlreadyExistsException {
        try {
            this.getReceta(recetaName);
            throw new ObjectAlreadyExistsException("The recipe "+recetaName+" already exists");
        } catch (InvalidNameException e) {
            this.recetas.put(recetaName.trim().toLowerCase(), receta);
        }
    }

    @Override
    public Receta getReceta(String recetaName) throws InvalidNameException {
        Receta receta = this.recetas.get(recetaName.trim().toLowerCase());
        if (receta == null) {
            throw new InvalidNameException("The recipe "+recetaName+" doesn't exist.");
        } else {
            return receta;
        }
    }

    @Override
    public void restockKitchen(PantryService pantryService) {
        pantryService.restockIngredientes();
        pantryService.renovarUtensilios();
    }

    @Override
    public void prepareKitchen(PantryService pantryService) {
        for (Receta receta: this.recetas.values()) {
            Set<Cocinable> ingredientes = receta.getIngredientes().values().stream()
                    .map(obj -> new Ingrediente(obj.getNombre(), 0))
                    .map(Cocinable.class::cast)
                    .collect(Collectors.toSet());
            pantryService.addIngredientes(ingredientes);
            Set<Reutilizable> utensilios = receta.getUtensilios().values().stream()
                    .map(obj -> new Utensilio(obj.getNombre(), (obj.getVidaUtil()*20)))
                    .map(Reutilizable.class::cast)
                    .collect(Collectors.toSet());
            pantryService.addUtensilios(utensilios);
        }
    }

    @Override
    public String getMissingItems(String recetaName, PantryService pantryService) throws InvalidNameException {
        StringBuilder missingItems = new StringBuilder();
        Receta receta = this.getReceta(recetaName);
        try {
            pantryService.verifyStock(new HashSet<>(receta.getIngredientes().values()));
        } catch (StockInsuficienteException e) {
            missingItems.append("\n").append(e.getMessage());
        }
        try {
            pantryService.verifyVidaUtil(new HashSet<>(receta.getUtensilios().values()));
        } catch (VidaUtilInsuficienteException e) {
            missingItems.append("\n").append(e.getMessage());
        }
        return missingItems.toString();
    }

    @Override
    public String makeReceta(String recetaName, PantryService pantryService) throws InvalidNameException, MissingObjectException {
        Receta receta = this.getReceta(recetaName);
        Set<Cocinable> ingredientes = new HashSet<>(receta.getIngredientes().values());
        Set<Reutilizable> utensilios = new HashSet<>(receta.getUtensilios().values());
        try {
            pantryService.verifyStock(ingredientes);
            pantryService.verifyVidaUtil(utensilios);
        } catch (StockInsuficienteException | VidaUtilInsuficienteException e) {
            this.restockKitchen(pantryService);
        }
        boolean restockFailFlag = false;
        boolean renewFailFlag = false;
        while (true) {
            try {
                pantryService.useIngredientes(ingredientes);
                pantryService.useUtensilios(utensilios);
                break;
            } catch (StockInsuficienteException e) {
                if (restockFailFlag) { throw new MissingObjectException(
                            "Verify all items needed for the recipe exists executing 'prepareKitchen()'"); }
                Set<Cocinable> missingIngredientes = pantryService.getMissingIngredientes(ingredientes);
                pantryService.restockIngredientes(missingIngredientes);
                restockFailFlag = true;
            } catch (VidaUtilInsuficienteException e) {
                if (renewFailFlag) { throw new MissingObjectException(
                        "Verify all items needed for the recipe exists executing 'prepareKitchen()'"); }
                Set<Reutilizable> missingUtensilios = pantryService.getMissingUtensilios(utensilios);
                pantryService.renovarUtensilios(missingUtensilios);
                renewFailFlag = true;
            }
        }
        return this.getReceta(recetaName).getPreparacion();
    }

    @Override
    public String showPantryStatus(PantryService pantryService) {
        return pantryService.getDespensa().toString();
    }
}
