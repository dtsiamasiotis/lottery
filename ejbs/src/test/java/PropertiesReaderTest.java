import org.junit.jupiter.api.Test;
import utils.PropertiesReader;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PropertiesReaderTest {
    @Test
    public void readPropertyTest() throws IOException {
        PropertiesReader reader = new PropertiesReader("application.properties");
        String property = reader.getProperty("informUsers.url");
        assertEquals("http://localhost", property);
    }
}
