package entity.customExceptions;

public class MissingObjectException extends Exception {
    public MissingObjectException(String mensaje) {
        super(mensaje);
    }
}
