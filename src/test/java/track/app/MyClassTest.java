package track.app;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.context.TestPropertySources;

import static org.junit.jupiter.api.Assertions.*;


class MyClassTest {

     static MockEnvironment environment = new MockEnvironment();//Mockito.mock(Environment.class);

     @BeforeAll
     public static void init() {
         environment.withProperty("database", "0");
         environment.withProperty("name", "Bear");
     }

    @Test
    void when_get() {
         MyClass myClass = new MyClass(environment);
        assertEquals("0", environment.getProperty("database"));
        assertEquals("Bear", myClass.getSomething());
    }

}