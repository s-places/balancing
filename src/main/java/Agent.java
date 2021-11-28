import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author splaces.project@gmail.com
 * Describes some abstract Agent (subject and owner)
 */
public @Data
class Agent {

    /**
     * Кey field for indication and comparing of objects
     */
    private int id;

    /**
     * Flag that decrease priority of balancing to last
     */
    private boolean isLazy;

    /**
     * Flag that decrease priority of balancing
     */
    private boolean isLowPriority;

    /**
     * Set of Stuff that can be owned by current Agent
     */
    private Set<Stuff> canBeOwnedStuff;

    /**
     * Set of Stuff that owned be current Agent in current moment
     */
    private Set<Stuff> ownedStuff;

    /**
     * Main constructor
     */
    public Agent(int id) {
        this.id = id;
        this.canBeOwnedStuff = new HashSet<>();
        this.ownedStuff = new HashSet<>();
    }

    /**
     * @param o object that will be compared
     * @return result of comparing this and given object based on id
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Agent agent = (Agent) o;

        return id == agent.id;
    }

    /**
     * @return value based on current id
     */
    @Override
    public int hashCode() {
        return id;
    }
}
