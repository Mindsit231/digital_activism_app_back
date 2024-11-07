//package mindsit.digitalactivismapp.custom;
//
//import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
//import org.springframework.core.env.PropertiesPropertySource;
//import org.springframework.core.env.PropertySource;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.support.DefaultPropertySourceFactory;
//import org.springframework.core.io.support.EncodedResource;
//
//import java.io.IOException;
//import java.util.Properties;
//
//public class YamlPropertySourceFactory extends DefaultPropertySourceFactory {
//
//    @Override
//    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
//        Resource resourceFile = resource.getResource();
//        if (resourceFile.exists()) {
//            Properties properties = loadYamlIntoProperties(resourceFile);
//            String sourceName = name != null ? name : resourceFile.getFilename();
//            if(sourceName != null) {
//                return new PropertiesPropertySource(sourceName, properties);
//            }
//        }
//        return super.createPropertySource(name, resource);
//    }
//
//    private Properties loadYamlIntoProperties(Resource resource) throws IOException {
//        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
//        factory.setResources(resource);
//        factory.afterPropertiesSet();
//        return factory.getObject();
//    }
//}
