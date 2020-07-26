# Apache Kafka example for Spring Boot

##Routing TO Consumer
Routing can be achieved by relying on RoutingFunction available in Spring Cloud Function 3.0. 

All you need to do is enable it via _**--spring.cloud.stream.function.routing.enabled=true**_ application property or provide _**spring.cloud.function.routing-expression**_ property. Once enabled RoutingFunction will be bound to input destination receiving all the messages and route them to other functions based on the provided instruction.

```yaml
spring:
  cloud:
    function:
      definition: gsm1,gsm2 - consumer list
      routing-expression: headers['gsm'] - routing rule
    stream:
      function:
        routing:
          enabled: true
        definition: transform;sendTestData;receive;gsm1;gsm2;functionRouter
      bindings:
        transform-out-0:
          destination: xformed
        receive-in-0:
          destination: xformed
        transform-in-0:
          destination: testtock
        sendTestData-out-0:
          destination: testinput
        
        # a router that redirects incoming messages to the correct consumer depending on the header
        functionRouter-in-0:
          binder: kafka
          destination: testinput
          consumer:
            durableSubscription: true
            concurrency: 1
            autoCommitOffset: true
```