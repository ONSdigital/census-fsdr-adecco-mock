package uk.gov.ons.fsdr.adeccomock.controller.lws;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition;


@EnableWs
@Configuration
public class WSConfig extends WsConfigurerAdapter{

    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
      MessageDispatcherServlet servlet = new MessageDispatcherServlet();
      servlet.setApplicationContext(applicationContext);
      servlet.setTransformWsdlLocations(true);
      return new ServletRegistrationBean(servlet, "/lws/*");
    }

    @Bean(name = "InsertUpdatePerson4")
    public Wsdl11Definition defaultGenericOutgoingWsWsdl11Definition() {
      SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
      wsdl11Definition.setWsdl(new ClassPathResource("/wsdl/loneworkermanager.wsdl"));
      return wsdl11Definition;
    }
}
