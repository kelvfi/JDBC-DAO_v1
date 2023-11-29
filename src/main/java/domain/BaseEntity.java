package domain;

public abstract class BaseEntity { // Basisklasse für alle Entity´s
    private long id;

    public BaseEntity(Long id) {
        setId(id);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {

        if (id == null || id >= 0) { // Wenn null dann ok weil wir einen Neuen setzen. INSERT | Wenn >= auch ok weil schon vorhanden UPDATE
            this.id = id;
        } else {
            throw new InvalidValueException("ID muss größer gleich 0 sein!");
        }
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id +
                '}';
    }
}
