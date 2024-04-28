package entity.customExceptions;

public class InvalidNameException extends Exception {
    public InvalidNameException(String mensaje) {
        super(mensaje);
    }
}
