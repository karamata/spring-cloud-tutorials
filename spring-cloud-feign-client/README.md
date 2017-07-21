###Feign Client
To finalize our project with three dependent microservices, we will now implement an REST-consuming web application using Spring Netflix Feign Client.

Think of Feign as discovery-aware Spring RestTemplate using interfaces to communicate with endpoints. This interfaces will be automatically implemented at runtime and instead of service-urls, it is using service-names.

Without Feign we would have to autowire an instance of EurekaClient into our controller with which we could receive a service-information by service-name as an Application object.

We would use this Application to get a list of all instances of this service, pick a suitable one and use this InstanceInfo to get hostname and port. With this, we could do a standard request with any http client.

For example:

    @Autowired
    private EurekaClient eurekaClient;
     
    public void doRequest() {
        Application application = eurekaClient.getApplication("spring-cloud-eureka-client");
        InstanceInfo instanceInfo = application.getInstances().get(0);
        String hostname = instanceInfo.getHostName();
        int port = instanceInfo.getPort();
        // ...
    }
    
A RestTemplate can also be used to access Eureka client-services by name, but this topic is beyond this write-up.

To setup our Feign Client project, we’re going to add the following four dependencies to its pom.xml:

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-feign</artifactId>
        <version>1.1.5.RELEASE</version>
    </dependency>
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
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
        <version>1.4.0.RELEASE</version>
    </dependency>

The Feign Client is located in the spring-cloud-starter-feign package. To enable it, we have to annotate a @Configuration with @EnableFeignClients. To use it, we simply annotate an interface with @FeignClient(“service-name”) and auto-wire it into a controller.

A good method to create such Feign Clients is to create interfaces with @RequestMapping annotated methods and put them into a separate module. This way they can be shared between server and client. On server-side, you can implement them as @Controller, and on client-side, they can be extended and annotated as @FeignClient.

Furthermore, the spring-cloud-starter-eureka package needs to be included in the project and enabled by annotating the main application class with @EnableEurekaClient.

The spring-boot-starter-web and spring-boot-starter-thymeleaf dependencies are used to present a view, containing fetched data from our REST service.

This will be our Feign Client interface:

    @FeignClient("spring-cloud-eureka-client")
    public interface GreetingClient {
        @RequestMapping("/greeting")
        String greeting();
    }

Here we will implement the main application class which simultaneously acts as a controller:

    @SpringBootApplication
    @EnableEurekaClient
    @EnableFeignClients
    @Controller
    public class FeignClientApplication {
        @Autowired
        private GreetingClient greetingClient;
     
        public static void main(String[] args) {
            SpringApplication.run(FeignClientApplication.class, args);
        }
     
        @RequestMapping("/get-greeting")
        public String greeting(Model model) {
            model.addAttribute("greeting", greetingClient.greeting());
            return "greeting-view";
        }
    }
    
    
This will be the HTML template for our view:

    <!DOCTYPE html>
    <html xmlns:th="http://www.thymeleaf.org">
        <head>
            <title>Greeting Page</title>
        </head>
        <body>
            <h2 th:text="${greeting}"/>
        </body>
    </html>    
    
At least the application.yml configuration file is almost the same as in the previous step:

    spring:
      application:
        name: spring-cloud-eureka-feign-client
    server:
      port: 8080
    eureka:
      client:
        serviceUrl:
          defaultZone: ${EUREKA_URI:http://localhost:8761/eureka}
      
Now we can build and run this service. Finally, we’ll point our browser to http://localhost:8080/get-greeting and it should display something like the following:

###Conclusion
As we’ve seen, we’re now able to implement a service registry using Spring Netflix Eureka Server and register some Eureka Clients with it.

Because our Eureka Client from step 3 listens on a randomly chosen port, it doesn’t know its location without the information from the registry. With a Feign Client and our registry, we can locate and consume the REST service, even when the location changes.

Finally, we’ve got a big picture about using service discovery in a microservice architecture.

As usual, you’ll find the sources on GitHub, which also includes a set of Docker-related files for using with docker-compose to create containers from our project.          