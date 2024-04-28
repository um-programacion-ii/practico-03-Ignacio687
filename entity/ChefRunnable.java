package entity;

import entity.customExceptions.InvalidNameException;

public class ChefRunnable implements Runnable {
    private String recetaName;
    private Chef chef;

    public ChefRunnable() {
    }

    public ChefRunnable(String recetaName, Chef chef) {
        this.recetaName = recetaName;
        this.chef = chef;
    }

    public String getRecetaName() {
        return recetaName;
    }

    public void setRecetaName(String recetaName) {
        this.recetaName = recetaName;
    }

    public Chef getChef() {
        return chef;
    }

    public void setChef(Chef chef) {
        this.chef = chef;
    }

    @Override
    public void run() {
        System.out.println("\nChef "+this.chef.getNombre()+":\n");
        System.out.println("Receta: "+this.recetaName);
        try {
            this.chef.makeReceta(this.recetaName);
        } catch (InvalidNameException e) {
            throw new RuntimeException(e);
        }
    }
}
