package mindsit.digitalactivismapp.custom;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:custom.yml")
@Getter
public class CustomConfig {
}
