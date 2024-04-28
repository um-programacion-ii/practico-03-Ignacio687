package service;

import entity.Cocinable;
import entity.Despensa;
import entity.Reutilizable;
import entity.customExceptions.StockInsuficienteException;
import entity.customExceptions.VidaUtilInsuficienteException;

import java.util.List;
import java.util.Set;

public interface PantryService {

    Despensa getDespensa();

    void setDespensa(Despensa despensa);

    void verifyStock(Set<Cocinable> ingredienteSet) throws StockInsuficienteException;

    void verifyVidaUtil(Set<Reutilizable> utensilioSet) throws VidaUtilInsuficienteException;

    Set<Cocinable> getMissingIngredientes(Set<Cocinable> ingredienteSet);

    Set<Reutilizable> getMissingUtensilios(Set<Reutilizable> utensilioSet);

    void useIngredientes(Set<Cocinable> ingredientes) throws StockInsuficienteException;

    void useUtensilios(Set<Reutilizable> utensilios) throws VidaUtilInsuficienteException;

    void renovarUtensilios();

    void renovarUtensilios(Set<Reutilizable> utensilios);

    void restockIngredientes();

    void restockIngredientes(Set<Cocinable> ingredientes);

    void addIngredientes(Set<Cocinable> ingredientes);

    void addUtensilios(Set<Reutilizable> utensilios);

}
