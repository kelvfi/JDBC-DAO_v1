package dataaccess;

import java.util.Optional;

// Typ Informationen T, I
public interface BaseRepository<T, I> {

    Optional<T> insert(T entity); // Optional = entweder etwas drin oder nicht, wenn erfolgreich etwas enthalten wenn nicht dann leer
}
