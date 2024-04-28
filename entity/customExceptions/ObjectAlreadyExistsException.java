package entity.customExceptions;

public class ObjectAlreadyExistsException extends Exception{
    public ObjectAlreadyExistsException(String mensaje) {
        super(mensaje);
    }
}
