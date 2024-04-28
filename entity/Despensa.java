package entity;

import entity.customExceptions.InvalidNameException;
import entity.customExceptions.ObjectAlreadyExistsException;
import entity.customExceptions.StockInsuficienteException;
import entity.customExceptions.VidaUtilInsuficienteException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Despensa {
    private Map<String, Despensable> despensables;

    public Despensa() {
        this.despensables = new HashMap<>();
    }

    public Despensa(Map<String, Despensable> despensables) {
        this.despensables = despensables;
    }

    public Map<String, Despensable> getDespensables() {
        return despensables;
    }

    public void setDespensables(Map<String, Despensable> despensables) {
        this.despensables = despensables;
    }

    @Override
    public String toString() {
        return "\nIngredientes en despensa:" + showItems(getMapOf(Cocinable.class, this.despensables))
                +"\n\nUtensilios en despensa:" +showItems(getMapOf(Reutilizable.class, this.despensables));
    }

    public static String showItems(Map<String, ? extends Despensable> despensableMap) {
        return despensableMap.values().stream()
                .map(Object::toString)
                .collect(Collectors.joining(", \n", "\n", ""));
    }

    public static <T extends Despensable> Map<String, T> getMapOf(Class<T> interfaz,
                                                                  Map<String, Despensable> despensables) {
        return despensables.values().stream()
                .filter(interfaz::isInstance)
                .map(interfaz::cast)
                .collect(Collectors.toMap(Despensable::getNombre, Function.identity()));
    }

    public void addIngrediente(Cocinable ingrediente) {
        try {
            Cocinable existingIngrediente = this.inspectIngrediente(ingrediente.getNombre());
            existingIngrediente.restock(ingrediente.getCantidad());
        } catch (InvalidNameException e) {
            this.despensables.put(ingrediente.getNombre().trim().toLowerCase(), ingrediente);
        }
    }

    public void addUtensilio(Reutilizable utensilio) throws ObjectAlreadyExistsException {
        try {
            this.inspectUtensilio(utensilio.getNombre());
            throw new ObjectAlreadyExistsException(String.format("Utensilio %s already exists", utensilio.getNombre()));
        } catch (InvalidNameException e) {
            this.despensables.put(utensilio.getNombre().trim().toLowerCase(), utensilio);
        }
    }

    public void getIngrediente(String name, int amount) throws StockInsuficienteException, InvalidNameException {
        Cocinable ingrediente = this.inspectIngrediente(name);
        ingrediente.sacar(amount);
    }

    public void useUtensilio(String name, int amount) throws VidaUtilInsuficienteException, InvalidNameException {
        Reutilizable reutilizable = this.inspectUtensilio(name);
        reutilizable.use(amount);
    }

    public Cocinable inspectIngrediente(String name) throws InvalidNameException {
        Cocinable ingrediente = getMapOf(Cocinable.class, this.despensables).get(name.trim().toLowerCase());
        if (ingrediente != null) {
            return ingrediente;
        } else {
            throw new InvalidNameException("The ingredient "+name+" doesn't exist.");
        }
    }

    public Reutilizable inspectUtensilio(String name) throws InvalidNameException {
        Reutilizable utensilio = getMapOf(Reutilizable.class, this.despensables).get(name.trim().toLowerCase());
        if (utensilio != null) {
            return utensilio;
        } else {
            throw new InvalidNameException("The utensil " + name + " doesn't exist.");
        }
    }
}
