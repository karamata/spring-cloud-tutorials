###Overview

In this tutorial, we’ll introduce client-side service discovery via “Spring Cloud Netflix Eureka“.

Client-side service discovery allows services to find and communicate with each other without hard coding hostname and port. The only ‘fixed point’ in such an architecture consists of a service registry with which each service has to register.

A drawback is that all clients must implement a certain logic to interact with this fixed point. This assumes an additional network round trip before the actual request.

With Netflix Eureka each client can simultaneously act as a server, to replicate its status to a connected peer. In other words, a client retrieves a list of all connected peers of a service registry and makes all further requests to any other services through a load-balancing algorithm.

To be informed about the presence of a client, they have to send a heartbeat signal to the registry.

To achieve the goal of this write-up, we will implement three microservices:

* service registry (Eureka Server),
* REST service which registers itself at the registry (Eureka Client) and
* web-application, which is consuming the REST service as a registry-aware client (Spring Cloud Netflix Feign Client).

###Eureka Server
To implement a Eureka Server for using as service registry is as easy as: adding spring-cloud-starter-eureka-server to the dependencies, enable the Eureka Server in a @SpringBootApplication per annotate it with @EnableEurekaServer and configure some properties. But we’ll do it step by step.

Firstly we’ll create a new Maven project and put the dependencies into it. You have to notice that we’re importing the spring-cloud-starter-parent to all projects described in this tutorial:

    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-eureka-server</artifactId>
        <version>1.1.5.RELEASE</version>
    </dependency>
     
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-starter-parent</artifactId>
                <version>Brixton.SR4</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

Next, we’re creating the main application class:

    @SpringBootApplication
    @EnableEurekaServer
    public class EurekaServerApplication {
        public static void main(String[] args) {
            SpringApplication.run(EurekaServerApplication.class, args);
        }
    }

Finally, we’re configuring the properties in YAML format; so an application.yml will be our configuration file:

    server:
      port: 8761
    eureka:
      client:
        registerWithEureka: false
        fetchRegistry: false
    
Here we’re configuring an application port – 8761 is the default one for Eureka servers. We are telling the built-in Eureka Client not to register with ‘itself’, because our application should be acting as a server.

Now we will point our browser to http://localhost:8761 to view the Eureka dashboard, where we will later inspecting the registered instances.

At the moment, we see basic indicators such as status and health indicators.