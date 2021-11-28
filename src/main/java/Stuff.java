import lombok.Data;

/**
 * @author splaces.project@gmail.com
 * Describes some abstract Stuff
 */
public @Data
class Stuff {

    /**
     * Кey field for indication and comparing of objects
     */
    private int id;

    /**
     * State of object
     */
    private boolean isOwned;

    /**
     * If object can be owned by some Agent
     */
    private boolean isCanBeOwned;

    /**
     * Main constructor
     */
    public Stuff(int id) {
        this.id = id;
    }

    /**
     * @param o object that will be compared
     * @return result of comparing this and given object based on id
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stuff stuff = (Stuff) o;

        return id == stuff.id;
    }

    /**
     * @return value based on current id
     */
    @Override
    public int hashCode() {
        return id;
    }
}
