package service;

import entity.*;
import entity.customExceptions.InvalidNameException;
import entity.customExceptions.ObjectAlreadyExistsException;
import entity.customExceptions.StockInsuficienteException;
import entity.customExceptions.VidaUtilInsuficienteException;
import entity.recetas.*;

import javax.print.attribute.standard.ReferenceUriSchemesSupported;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main (String[] args) {
        // ################ TP 2 PUNTO 2 ################
        System.out.print("\u001B[31m");
        System.out.println("\nTP2 Punto2:\n");
        System.out.print("\u001B[0m");
        Map<String, Receta> recetas = new HashMap<>() {{
            put("fideos", new Fideos());
            put("huevo duro", new HuevoDuro());
            put("pizza", new Pizza());
        }};
        CocinaService cocinaService = new CocinaService(recetas, new DespensaService());
        Chef chef = new Chef("Fernando", 2, cocinaService);
        System.out.println("\nPreparando la cocina para su uso...\n");
        cocinaService.prepareKitchen();
        System.out.println("Estado de la despensa:"+cocinaService.showPantryStatus());
        System.out.println("\nEl "+ chef + " va a preparar las siguientes");
        chef.showAvailableRecipes();
        chef.makeRecetas();
        System.out.println("\nUn cliente pidió una receta nueva: 'ensalada'\n");
        try {
            cocinaService.addReceta("Ensalada ", new Ensalada());
            System.out.println(cocinaService.getReceta("ensalada"));
            chef.makeReceta("ensalada");
        } catch (ObjectAlreadyExistsException | InvalidNameException e) {
            throw new RuntimeException(e);
        }
        System.out.println("\nEl chef quemó una olla y rompió un tenedor...");
        try {
            Reutilizable olla = cocinaService.getDespensaService().getDespensa().inspectUtensilio("olla");
            olla.use(olla.getVidaUtil());
            Reutilizable cuchara = cocinaService.getDespensaService().getDespensa().inspectUtensilio("tenedor");
            cuchara.use(cuchara.getVidaUtil());
            System.out.println("\nEl último cliente pidió unos fideos...");
            chef.makeReceta("FiDeos   ");
        } catch (InvalidNameException | VidaUtilInsuficienteException e) {
            throw new RuntimeException(e);
        }
    }
}