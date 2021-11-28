import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * @author splaces.project@gmail.com
 * Intagration tests for balancing application
 */
public class WorkloadIntegrationTest {

    /**
     * Selected object for specified testing
     */
    private Agent testAgent;

    /**
     * List of all Stuff
     */
    private List<Stuff> allStuff;

    /**
     * List of all Agents
     */
    private List<Agent> allAgents;

    /**
     * Singleton object
     */
    private Workload workload;

    /**
     * Makes initialization before every test
     */
    public @Before
    void setUp() {
        testAgent = new Agent(1);
        testAgent.setLazy(true);
        allAgents = new LinkedList<>();
        allStuff = new LinkedList<>();
        allAgents.add(testAgent);
        for (int i = 2; i < 11; i++) {
            this.allAgents.add(new Agent(i));
        }
        for (int i = 0; i < 20; i++) {
            this.allStuff.add(new Stuff(i));
        }
        this.workload = Workload.getWorkload(allAgents, allStuff);
    }

    /**
     * Makes reset after every test
     */
    public @After
    void tearDown() {
        Workload.setWorkload(null);
        this.workload = null;
        this.allAgents = null;
        this.allStuff = null;
    }

    /**
     * Makes test of initialization
     */
    public @Test
    void integrationLogicInitialize() {

        assertTrue(testAgent.getCanBeOwnedStuff().isEmpty()
                && testAgent.getOwnedStuff().isEmpty());
        assertTrue(workload.getFreeStuff().isEmpty()
                && workload.getNotOwnedStuff().isEmpty());
        workload.removeLastAgent();

    }

    /**
     * Makes test of removing Agent
     */
    public @Test
    void integrationLogicRemoveAgent() {

        workload.removeLastAgent();

        assertTrue(workload.getFreeStuff().isEmpty()
                && workload.getNotOwnedStuff().isEmpty());

    }

    /**
     * Makes test of adding Agent
     */
    public @Test
    void integrationLogicAddAgent() {

        workload.addAgent(new Agent(999));

        assertTrue(workload.getAllStuff().size() * 0.25 > workload.getFreeStuff().size()
                && workload.getAllStuff().size() * 0.25 > workload.getNotOwnedStuff().size());

    }
}

