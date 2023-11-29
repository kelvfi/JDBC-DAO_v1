package dataaccess;

import java.util.List;
import java.util.Optional;

// Typ Informationen T, I | Platzhalter zur Mitgabe | I = Schlüsseltyp T = Entity
public interface BaseRepository<T, I> {

    Optional<T> insert(T entity); // Optional = entweder etwas drin oder nicht, wenn erfolgreich dann ist etwas enthalten, wenn nicht, dann leer
    Optional<T> getById(I id); // Man gibt I mit, weil man z.B. einen Long noch mitgeben möchte
    List<T> getAll();
    Optional<T> update(T entity);
    void deleteById(I id);

}
