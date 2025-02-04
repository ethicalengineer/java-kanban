package exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String entity, long id) {
      super("Entity: " + entity + " with ID " + id + " not found.");
    }
}
