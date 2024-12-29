//package EduStore.user_service.config;
//
//import org.springframework.boot.web.servlet.MultipartConfigFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import jakarta.servlet.MultipartConfigElement;
//import org.springframework.util.unit.DataSize;
//
//@Configuration
//public class MultipartConfig {
//
//    @Bean
//    public MultipartConfigElement multipartConfigElement() {
//        MultipartConfigFactory factory = new MultipartConfigFactory();
//        factory.setMaxFileSize(DataSize.ofMegabytes(10));
//        factory.setMaxRequestSize(DataSize.ofMegabytes(10));
//        return factory.createMultipartConfig();
//    }
//}