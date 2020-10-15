package utils;

import javax.ejb.Stateless;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Stateless
public class PropertiesReader {
    private Properties properties;

    public PropertiesReader() throws IOException{
        InputStream is = getClass().getClassLoader()
                .getResourceAsStream("application.properties");
        this.properties = new Properties();
        this.properties.load(is);
    }

    public String getProperty(String propertyName) {
        return this.properties.getProperty(propertyName);
    }
}
