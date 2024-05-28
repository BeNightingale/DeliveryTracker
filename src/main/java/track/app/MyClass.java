package track.app;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;


@PropertySource("classpath:user.properties")
@PropertySource("classpath:dict.properties")
@Component
public class MyClass {
    final Environment environment;

    public MyClass(Environment environment) {
        this.environment = environment;
    }

    public String getSomething() {
        return environment.getProperty("name");// + "była na wakacjach";
      //  String[] stringList = environment.getProperty("address", "Paris ").split(", ");
     //   System.out.println(stringList[2] + " podobała się");
    }
}
