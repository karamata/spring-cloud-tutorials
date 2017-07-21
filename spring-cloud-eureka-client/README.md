###Eureka Client
For a @SpringBootApplication to be discovery-aware, we have to include some Spring Discovery Client (for example spring-cloud-starter-eureka) into our classpath.

Then we need to annotate a @Configuration with either @EnableDiscoveryClient or @EnableEurekaClient.

The latter tells Spring Boot to use Spring Netflix Eureka for service discovery explicitly. To fill our client application with some sample-life, we’ll also include the spring-boot-starter-web package in the pom.xml and implement a REST controller.

But first, we will add the dependencies:

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-eureka</artifactId>
        <version>1.1.5.RELEASE</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <version>1.4.0.RELEASE</version>
    </dependency>
    
Here we will implement the main application class:

    @SpringBootApplication
    @EnableEurekaClient
    @RestController
    public class EurekaClientApplication {
        public static void main(String[] args) {
            SpringApplication.run(EurekaClientApplication.class, args);
        }
     
        @RequestMapping("/greeting")
        public String greeting() {
            return "Hello from EurekaClient!";
        }
    }
    
    
Next, we have to set-up an application.yml with a configured Spring application name to uniquely identify our client in the list of registered applications.

We can let Spring Boot choose a random port for us because later we are accessing this service with its name, and finally, we have to tell our client, where it has to locate the registry:

    spring:
      application:
        name: spring-cloud-eureka-client
    server:
      port: 0
    eureka:
      client:
        serviceUrl:
          defaultZone: ${EUREKA_URI:http://localhost:8761/eureka}
      instance:
        preferIpAddress: true
    
When we decided to set up our Eureka Client this way, we had in mind that this kind of service should later be easily scalable.

Now we will run the client and point our browser to http://localhost:8761 again, to see its registration status on the Eureka Dashboard. By using the Dashboard, we can do further configuration e.g. link the homepage of a registered client with the Dashboard for administrative purposes. The configuration options, however, are beyond the scope of this article.

    