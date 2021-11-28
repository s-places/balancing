import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.lang.System.err;

/**
 * @author splaces.project@gmail.com
 * Utility class that contains all main logic
 */
public @Data
class Workload {

    /**
     * Static link to singleton object
     */
    private @Getter
    @Setter
    static Workload workload;

    /**
     * Special coefficient for balancing optimization
     */
    private double modifierCoefficientForOptimization = 0.75;

    /**
     * List of all Agents in object
     */
    private List<Agent> allAgents;

    /**
     * List of all Stuff in object
     */
    private List<Stuff> allStuff;

    /**
     * List of Stuff that cannot be owned anyone Agent
     */
    private Set<Stuff> freeStuff;

    /**
     * List of Stuff that can be owned but have not owned by Agent
     */
    private Set<Stuff> notOwnedStuff;

    /**
     * Constructor for singleton object
     *
     * @param allAgents list of all Agents in object
     * @param allStuff  list of all Stuff in object
     */
    private Workload(List<Agent> allAgents, List<Stuff> allStuff) {
        this.allAgents = allAgents;
        this.allStuff = allStuff;
        this.freeStuff = new LinkedHashSet<>();
        this.notOwnedStuff = new LinkedHashSet<>();
        runWorkload();
    }

    /**
     * Create and @return static link to singleton object
     *
     * @param allAgents list of all Agents in object
     * @param allStuff  list of all Stuff in object
     */
    public static Workload getWorkload(List<Agent> allAgents, List<Stuff> allStuff) {
        return workload != null ? workload : (workload = new Workload(allAgents, allStuff));
    }

    /**
     * Prints current state
     */
    public void getInfo() {
        err.println("allAgents");
        getAllAgents().forEach(err::println);
        err.println("notOwnedStuff");
        getNotOwnedStuff().forEach(err::println);
        err.println("freeStuff");
        getFreeStuff().forEach(err::println);
    }

    /**
     * Runs initialization and balancing
     */
    private void runWorkload() {
        initialize();
        balancing();
    }

    /**
     * Adds agent
     *
     * @param agent object that will be added
     */
    public void addAgent(Agent agent) {
        allAgents.add(agent);
        optimizing();
        runWorkload();
    }

    /**
     * Removes last agent in list
     */
    public void removeLastAgent() {
        Agent agent = allAgents.get(allAgents.size() - 1);
        removeAgent(agent);
    }

    /**
     * Removes specified agent
     *
     * @param agent object that will be removed
     */
    public void removeAgent(Agent agent) {
        if (agent.getCanBeOwnedStuff().isEmpty() && agent.getOwnedStuff().isEmpty())
            allAgents.remove(agent);
        else {
            allAgents.remove(agent);
            rebalancing(agent);
            runWorkload();
        }
    }

    /**
     * Initializes of freeStuff and notOwnedStuff lists
     */
    private void initialize() {
        err.println("Initialization started");
        for (Stuff stuff : allStuff) if (!stuff.isCanBeOwned() && !stuff.isOwned()) freeStuff.add(stuff);
        while (!freeStuff.iterator().hasNext() && !freeStuff.isEmpty())
            for (Agent agent : allAgents) {
                Stuff stuff = freeStuff.iterator().hasNext() && !freeStuff.isEmpty() ? freeStuff.iterator().next() : null;
                if (stuff == null) break;
                else {
                    stuff.setCanBeOwned(true);
                }
                if (stuff.isCanBeOwned() && !agent.isLazy() && !agent.isLowPriority()) {
                    agent.getCanBeOwnedStuff().add(stuff);
                    notOwnedStuff.add(stuff);
                }
                freeStuff.remove(stuff);
            }
        while (freeStuff.iterator().hasNext() && !freeStuff.isEmpty())
            for (Agent agent : allAgents) {
                Stuff stuff = freeStuff.iterator().hasNext() && !freeStuff.isEmpty() ? freeStuff.iterator().next() : null;
                if (stuff == null) break;
                else {
                    stuff.setCanBeOwned(true);
                }
                if (stuff.isCanBeOwned() && !agent.isLazy()) {
                    agent.getCanBeOwnedStuff().add(stuff);
                    notOwnedStuff.add(stuff);
                }
                freeStuff.remove(stuff);
            }

        while (freeStuff.iterator().hasNext() && !freeStuff.isEmpty())
            for (Agent agent : allAgents) {
                Stuff stuff = freeStuff.iterator().hasNext() && !freeStuff.isEmpty() ? freeStuff.iterator().next() : null;
                if (stuff == null) break;
                else {
                    stuff.setCanBeOwned(true);
                }
                if (stuff.isCanBeOwned()) {
                    agent.getCanBeOwnedStuff().add(stuff);
                    notOwnedStuff.add(stuff);
                }
                freeStuff.remove(stuff);
            }

        err.println("Initialization complete");
    }

    /**
     * Makes balancing between Agents
     */
    private void balancing() {
        err.println("Balancing started");
        for (Agent agent : allAgents) {
            for (Stuff stuff : agent.getCanBeOwnedStuff())
                if (!agent.isLazy() && !agent.isLowPriority() && notOwnedStuff.contains(stuff)) {
                    stuffOwning(agent, stuff);
                }
        }
        for (Agent agent : allAgents) {
            for (Stuff stuff : agent.getCanBeOwnedStuff())
                if (!agent.isLazy() && notOwnedStuff.contains(stuff)) {
                    stuffOwning(agent, stuff);
                }
        }
        for (Agent agent : allAgents) {
            for (Stuff stuff : agent.getCanBeOwnedStuff())
                if (notOwnedStuff.contains(stuff)) {
                    stuffOwning(agent, stuff);
                }
        }
        err.println("Balancing complete");
    }

    /**
     * Makes rebalancing before Agent will be deleted
     *
     * @param agent - object that will be deleted and their
     *              Stuff will be rebalanced
     */
    private void rebalancing(Agent agent) {
        err.println("Rebalancing started");
        for (Stuff stuff : agent.getOwnedStuff()) {
            stuff.setOwned(false);
            stuff.setCanBeOwned(false);
        }
        for (Stuff stuff : agent.getCanBeOwnedStuff()) {
            stuff.setOwned(false);
            stuff.setCanBeOwned(false);
            freeStuff.add(stuff);
        }
        err.println("Rebalancing complete");
    }

    /**
     * Makes optimization between Agents
     */
    private void optimizing() {

        err.println("Optimizing started");

        int modifierSizeCanBeOwned = (int) Math.round((allAgents.stream()
                .mapToDouble(agent -> agent.getCanBeOwnedStuff().size()).sum() / allAgents.size())
                * modifierCoefficientForOptimization);
        int modifierSizeOwned = (int) Math.round((allAgents.stream()
                .mapToDouble(agent -> agent.getOwnedStuff().size()).sum() / allAgents.size())
                * modifierCoefficientForOptimization);

        for (Agent agent : allAgents) {
            while (agent.getOwnedStuff().size() > modifierSizeOwned
                    && agent.getOwnedStuff().iterator().hasNext()) {
                Stuff stuff = agent.getOwnedStuff().iterator().next();
                agent.getOwnedStuff().remove(stuff);
                stuff.setOwned(false);
                notOwnedStuff.add(stuff);
            }
        }
        for (Agent agent : allAgents) {
            while (agent.getCanBeOwnedStuff().size() > modifierSizeCanBeOwned
                    && agent.getCanBeOwnedStuff().iterator().hasNext()) {
                Stuff stuff = agent.getCanBeOwnedStuff().iterator().next();
                agent.getCanBeOwnedStuff().remove(stuff);
                stuff.setCanBeOwned(false);
                freeStuff.add(stuff);
            }
        }
        err.println("Optimizing complete");
    }

    /**
     * Makes the Stuff owned by specified Agent
     *
     * @param agent object that owning
     * @param stuff object that will be owned
     */
    private void stuffOwning(Agent agent, Stuff stuff) {
        agent.getOwnedStuff().add(stuff);
        notOwnedStuff.remove(stuff);
        stuff.setOwned(true);
        stuff.setCanBeOwned(false);
    }

}

