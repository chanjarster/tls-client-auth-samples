package me.chanjar.client.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class RestTemplateExample {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(RestTemplateExample.class, args);

    RestTemplate unknownCADisableServerAuth = context.getBean("unknownCADisableServerAuth", RestTemplate.class);
    System.out.println(unknownCADisableServerAuth.getForObject("https://localhost:8443", String.class));

    RestTemplate unknownCAEnableServerAuth = context.getBean("unknownCAEnableServerAuth", RestTemplate.class);
    System.out.println(unknownCAEnableServerAuth.getForObject("https://localhost:8443", String.class));

    RestTemplate knownCA = context.getBean("knownCA", RestTemplate.class);
    System.out.println(knownCA.getForObject("https://baidu.com", String.class));

  }
}
