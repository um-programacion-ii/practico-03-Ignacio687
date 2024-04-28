package service;

import entity.*;
import entity.customExceptions.InvalidNameException;
import entity.customExceptions.ObjectAlreadyExistsException;
import entity.customExceptions.StockInsuficienteException;
import entity.customExceptions.VidaUtilInsuficienteException;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DespensaService implements PantryService{
    private Despensa despensa;

    public DespensaService() {
        this.despensa = new Despensa();
    }

    public DespensaService(Despensa despensa) {
        this.despensa = despensa;
    }

    @Override
    public Despensa getDespensa() {
        return despensa;
    }

    @Override
    public void setDespensa(Despensa despensa) {
        this.despensa = despensa;
    }

    @Override
    public String toString() {
        return "DespensaService: \n" + despensa;
    }

    @Override
    public void verifyStock(Set<Cocinable> ingredienteSet) throws StockInsuficienteException {
        Set<Cocinable> ingredientesFaltantes = this.getMissingIngredientes(ingredienteSet);
        if (!ingredientesFaltantes.isEmpty()) {
            throw new StockInsuficienteException("Faltan los siguientes ingredientes:  "
                    + Despensa.showItems(ingredientesFaltantes.stream()
                    .collect(Collectors.toMap(Cocinable::getNombre, Function.identity()))));
        }
    }

    @Override
    public void verifyVidaUtil(Set<Reutilizable> utensilioSet) throws VidaUtilInsuficienteException {
        Set<Reutilizable> utensiliosFaltantes = this.getMissingUtensilios(utensilioSet);
        if (!utensiliosFaltantes.isEmpty()) {
            throw new VidaUtilInsuficienteException("Tiempo faltante en los siguientes utensilios:  "
                    + Despensa.showItems(utensiliosFaltantes.stream()
                    .collect(Collectors.toMap(Reutilizable::getNombre, Function.identity()))));
        }
    }

    @Override
    public Set<Cocinable> getMissingIngredientes(Set<Cocinable> ingredienteSet) {
        Despensa ingredientesFaltantes = new Despensa();
        for (Cocinable ingrediente: ingredienteSet) {
            try {
                Cocinable ingredienteDisp = this.despensa.inspectIngrediente(ingrediente.getNombre());
                if (ingredienteDisp.getCantidad() < ingrediente.getCantidad()) {
                    ingredientesFaltantes.addIngrediente(new Ingrediente(ingrediente.getNombre(),
                            ingrediente.getCantidad()-ingredienteDisp.getCantidad()));
                }
            } catch (InvalidNameException e) {
                ingredientesFaltantes.addIngrediente(new Ingrediente(ingrediente.getNombre(),
                        ingrediente.getCantidad()));
            }
        }
        return ingredientesFaltantes.getDespensables().values().stream()
                .map(Cocinable.class::cast)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Reutilizable> getMissingUtensilios(Set<Reutilizable> utensilioSet) {
        Despensa utensiliosFaltantes = new Despensa();
        for (Reutilizable utensilio: utensilioSet) {
            try {
                Reutilizable utensilioDisp = this.despensa.inspectUtensilio(utensilio.getNombre());
                if (utensilioDisp.getVidaUtil() < utensilio.getVidaUtil()) {
                    utensiliosFaltantes.addUtensilio(new Utensilio(utensilio.getNombre(),
                            utensilio.getVidaUtil()-utensilioDisp.getVidaUtil()));
                }
            } catch (InvalidNameException e) {
                try {
                    utensiliosFaltantes.addUtensilio(new Utensilio(utensilio.getNombre(), utensilio.getVidaUtil()));
                } catch (ObjectAlreadyExistsException ex) {
                    throw new RuntimeException(ex);
                }
            } catch (ObjectAlreadyExistsException e) {
                throw new RuntimeException(e);
            }
        }
        return utensiliosFaltantes.getDespensables().values().stream()
                .map(Reutilizable.class::cast)
                .collect(Collectors.toSet());
    }

    @Override
    public void useIngredientes(Set<Cocinable> ingredientes) throws StockInsuficienteException {
        this.verifyStock(ingredientes);
        for (Cocinable ingrediente: ingredientes) {
            try {
                this.despensa.getIngrediente(ingrediente.getNombre(), ingrediente.getCantidad());
            } catch (InvalidNameException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void useUtensilios(Set<Reutilizable> utensilios) throws VidaUtilInsuficienteException {
        this.verifyVidaUtil(utensilios);
        for (Reutilizable utensilio: utensilios) {
            try {
                this.despensa.useUtensilio(utensilio.getNombre(), utensilio.getVidaUtil());
            } catch (InvalidNameException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void renovarUtensilios() {
       Despensa.getMapOf(Reutilizable.class, this.despensa.getDespensables()).values().stream()
               .filter(obj -> obj.getVidaUtil() < (obj.getvidaUtilInicial()*0.05))
               .forEach(Reutilizable::renew);
    }

    @Override
    public void renovarUtensilios(Set<Reutilizable> utensilios) {
        for (Reutilizable reutilizable: utensilios) {
            try {
                this.despensa.inspectUtensilio(reutilizable.getNombre()).renew();
            } catch (InvalidNameException e) {
                // Este catch queda vacío, ya que el objetivo del método es ignorar los objetos no existentes.
            }
        }
    }

    @Override
    public void restockIngredientes() {
        Despensa.getMapOf(Cocinable.class, this.despensa.getDespensables()).values().stream()
                .filter(obj -> obj.getCantidad() < 10)
                .forEach(obj -> obj.restock(10-obj.getCantidad()));
    }

    @Override
    public void restockIngredientes(Set<Cocinable> ingredientes) {
        for (Cocinable cocinable: ingredientes) {
            try {
                this.despensa.inspectIngrediente(cocinable.getNombre()).restock(cocinable.getCantidad());
            } catch (InvalidNameException e) {
                // Este catch queda vacío, ya que el objetivo del método es ignorar los objetos no existentes.
            }
        }
    }

    @Override
    public void addIngredientes(Set<Cocinable> ingredientes) {
        for (Cocinable cocinable: ingredientes) {
            try {
                this.despensa.inspectIngrediente(cocinable.getNombre());
            } catch (InvalidNameException e) {
                this.despensa.addIngrediente(cocinable);
            }
        }
    }

    @Override
    public void addUtensilios(Set<Reutilizable> utensilios) {
        for (Reutilizable reutilizable: utensilios) {
            try {
                this.despensa.inspectUtensilio(reutilizable.getNombre());
            } catch (InvalidNameException e) {
                try {
                    this.despensa.addUtensilio(reutilizable);
                } catch (ObjectAlreadyExistsException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
}
