import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import vbn.state.value.BooleanSymbol;

public class ComputeTest {

    @BeforeEach
    void setUp() {
        clear();
    }

    @Test
    void testSimpleCompute() {
        var boolX = new BooleanSymbol("x", true);
        var boolY = new BooleanSymbol("y", false);

        pushSymbol(boolX);
        pushSymbol(boolY);
    }
}
