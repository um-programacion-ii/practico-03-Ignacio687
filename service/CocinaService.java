package service;

import entity.*;
import entity.customExceptions.*;
import entity.recetas.Receta;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class CocinaService implements KitchenService{
    private Map<String, Receta> recetas;
    private DespensaService despensaService;

    public CocinaService() {
        this.recetas = new HashMap<>();
        this.despensaService = new DespensaService();
    }

    public CocinaService(Map<String, Receta> recetas, DespensaService despensaService) {
        this.recetas = recetas;
        this.despensaService = despensaService;
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
    public DespensaService getDespensaService() {
        return despensaService;
    }

    @Override
    public void setDespensaService(DespensaService despensaService) {
        this.despensaService = despensaService;
    }

    @Override
    public String toString() {
        return "CocinaService, " + KitchenService.showRecetas(this.recetas) + this.despensaService;
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
    public void restockKitchen() {
        this.despensaService.restockIngredientes();
        this.despensaService.renovarUtensilios();
    }

    @Override
    public void prepareKitchen() {
        for (Receta receta: this.recetas.values()) {
            Set<Cocinable> ingredientes = receta.getIngredientes().values().stream()
                    .map(obj -> new Ingrediente(obj.getNombre(), 0))
                    .map(Cocinable.class::cast)
                    .collect(Collectors.toSet());
            this.despensaService.addIngredientes(ingredientes);
            Set<Reutilizable> utensilios = receta.getUtensilios().values().stream()
                    .map(obj -> new Utensilio(obj.getNombre(), (obj.getVidaUtil()*20)))
                    .map(Reutilizable.class::cast)
                    .collect(Collectors.toSet());
            this.despensaService.addUtensilios(utensilios);
        }
    }

    public String getMissingItems(String recetaName) throws InvalidNameException {
        StringBuilder missingItems = new StringBuilder();
        Receta receta = this.getReceta(recetaName);
        try {
            this.despensaService.verifyStock(new HashSet<>(receta.getIngredientes().values()));
        } catch (StockInsuficienteException e) {
            missingItems.append("\n").append(e.getMessage());
        }
        try {
            this.despensaService.verifyVidaUtil(new HashSet<>(receta.getUtensilios().values()));
        } catch (VidaUtilInsuficienteException e) {
            missingItems.append("\n").append(e.getMessage());
        }
        return missingItems.toString();
    }

    @Override
    public String makeReceta(String recetaName) throws InvalidNameException, MissingObjectException {
        Receta receta = this.getReceta(recetaName);
        Set<Cocinable> ingredientes = new HashSet<>(receta.getIngredientes().values());
        Set<Reutilizable> utensilios = new HashSet<>(receta.getUtensilios().values());
        try {
            this.despensaService.verifyStock(ingredientes);
            this.despensaService.verifyVidaUtil(utensilios);
        } catch (StockInsuficienteException | VidaUtilInsuficienteException e) {
            this.restockKitchen();
        }
        boolean restockFailFlag = false;
        boolean renewFailFlag = false;
        while (true) {
            try {
                this.despensaService.useIngredientes(ingredientes);
                this.despensaService.useUtensilios(utensilios);
                break;
            } catch (StockInsuficienteException e) {
                if (restockFailFlag) { throw new MissingObjectException(
                            "Verify all items needed for the recipe exists executing 'prepareKitchen()'"); }
                Set<Cocinable> missingIngredientes = this.despensaService.getMissingIngredientes(ingredientes);
                this.despensaService.restockIngredientes(missingIngredientes);
                restockFailFlag = true;
            } catch (VidaUtilInsuficienteException e) {
                if (renewFailFlag) { throw new MissingObjectException(
                        "Verify all items needed for the recipe exists executing 'prepareKitchen()'"); }
                Set<Reutilizable> missingUtensilios = this.despensaService.getMissingUtensilios(utensilios);
                this.despensaService.renovarUtensilios(missingUtensilios);
                renewFailFlag = true;
            }
        }
        return this.getReceta(recetaName).getPreparacion();
    }

    @Override
    public String showPantryStatus() {
        return this.despensaService.getDespensa().toString();
    }
}
