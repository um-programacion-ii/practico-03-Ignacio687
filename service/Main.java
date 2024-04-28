package service;

import entity.*;
import entity.recetas.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main (String[] args) {
        // ################ TP 3 ################
        System.out.print("\u001B[31m");
        System.out.println("\nTP3:\n");
        System.out.print("\u001B[0m");
        Map<String, Receta> recetas = new HashMap<>() {{
            put("ensalada", new Ensalada());
            put("fideos", new Fideos());
            put("huevo duro", new HuevoDuro());
            put("pizza", new Pizza());
        }};
        KitchenService cocinaService = CocinaService.getInstance();
        cocinaService.setRecetas(recetas);
        System.out.println("\n\n"+KitchenService.showRecetas(cocinaService.getRecetas())+"\n\n");
        // ################ Lunes a Jueves ################
        System.out.print("\u001B[32m");
        System.out.println("\nEjemplo de comportamiento de Lunes a Jueves:\n");
        System.out.print("\u001B[0m");
        Map<String, Chef> chefsMap3 = new HashMap<>();
        for (String name: new String[]{"Pedro", "Juan", "Pepe"}) {
            chefsMap3.put(name, new Chef(name, new Random().nextInt(6), cocinaService, new DespensaService()));
        }
        System.out.println("\nCada chef va a preparar una receta:  \n\n");
        ExecutorService executorService3 = Executors.newFixedThreadPool(3);
        List<String> nombresRecetas = recetas.keySet().stream().toList();
        for (Chef chef: chefsMap3.values()) {
            executorService3.execute(chef.getCallableMakeReceta(nombresRecetas
                    .get(new Random().nextInt(nombresRecetas.size()))));
        }
        try {
            executorService3.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        executorService3.shutdown();
        // ################ Viernes a Domingos y Feriados ################
        System.out.print("\u001B[32m");
        System.out.println("\nEjemplo de comportamiento de Viernes a Domingos y Feriados:\n");
        System.out.print("\u001B[0m");
        Map<String, Chef> chefsMap5 = new HashMap<>();
        for (String name: new String[]{"Jose", "Abril", "Sofia", "Matias", "Lisa"}) {
            chefsMap5.put(name, new Chef(name, new Random().nextInt(6), cocinaService, new DespensaService()));
        }
        System.out.println("\nCada chef va a preparar una receta:  \n\n");
        ExecutorService executorService5 = Executors.newFixedThreadPool(5);
        for (Chef chef: chefsMap5.values()) {
            executorService5.execute(chef.getCallableMakeReceta(nombresRecetas
                    .get(new Random().nextInt(nombresRecetas.size()))));
        }
        executorService5.shutdown();
    }
}