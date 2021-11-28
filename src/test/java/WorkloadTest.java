import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author splaces.project@gmail.com
 * Unit tests for balancing application
 */
public class WorkloadTest {

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
     * Test current singleton object
     */
    public @Test
    void getWorkload() {
        assertNotNull(workload);
    }

    /**
     * Test current state of attributes
     */
    public @Test
    void getInfo() {
        assertTrue(workload.getAllAgents().size() >= 7);
        assertTrue(workload.getAllStuff().size() >= 7);
        assertTrue(workload.getAllAgents().contains(testAgent));
    }

    /**
     * Test adding specified Agent
     */
    public @Test
    void addAgent() {
        int allAgentSizeBeforeAdd = workload.getAllAgents().size();
        workload.addAgent(new Agent(999));
        assertTrue(allAgentSizeBeforeAdd + 1 == workload.getAllAgents().size());
    }

    /**
     * Test removing last Agent in the list
     */
    public @Test
    void removeLastAgent() {
        int allAgentSizeBeforeDelete = workload.getAllAgents().size();
        workload.removeLastAgent();
        assertTrue(allAgentSizeBeforeDelete - 1 == workload.getAllAgents().size());
    }

    /**
     * Test removing specified Agent
     */
    public @Test
    void removeAgent() {
        int allAgentSizeBeforeDelete = workload.getAllAgents().size();
        workload.removeAgent(testAgent);
        assertTrue(allAgentSizeBeforeDelete - 1 == workload.getAllAgents().size());
        assertFalse(workload.getAllAgents().contains(testAgent));
    }

}
