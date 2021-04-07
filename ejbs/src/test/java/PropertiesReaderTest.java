import org.junit.jupiter.api.Test;
import utils.PropertiesReader;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PropertiesReaderTest {
    @Test
    public void readPropertyTest() throws IOException {
        PropertiesReader reader = new PropertiesReader();
        String property = reader.getProperty("informWinners.url");
        assertEquals("http://localhost:3500/informWinners", property);
    }
}
