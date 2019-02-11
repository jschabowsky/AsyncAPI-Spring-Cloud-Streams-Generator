Purpose of this Generator is to ease development of Spring Cloud Streams microservices by supporting defining them in AsyncAPI. The problem was decomposed into the following parts:
1) Generate Project Structure 
Project structure and build is taken care of by the Spring Initializr project, and thus was reused. 
2) Generate POJO's based off of the input/output messages 
Since AsyncAPI supports defining the message payloads as JSON Schema, this enables code generators to generate the POJOs which represent these data structures. Since I am not a code generator wizard, I leveraged dentrassi's java generator and commented out a few lines so that it only generated the message payload objects. That is why this code contains those packages.
3) Generate SCS application 
In order to generate the SCS Application, the associated java class must have the proper annotations specifying whether it is a source, sink or processor. In addition the method must so that it has a method signature which either sink, source or processor (@EnableBinding) and matches the method to the appropriate @InboundChannelAdapter, @StreamListener or @Output. This enables the user to simply add their business logic vs having to understand the mechanics of SCS
4) Generate Application.YAML - binds the application to the runtime though binder interface 
The Application.yaml file purpose is to map the INPUTS/OUTPUTS to the underling binding implementation. In this case, this generator works with Solace's but could easily be extended to support others. 

How to run it: 
1) update the application.propertes (Since its in src/main/resources, it expects it to be in the jar) An example is shown below: 
scs.scsType=PROCESSOR 
scs.packageName=com.solace.test.processor
scs.baseDir=SCSProcessorproject
scs.asyncAPIfile=asyncapi-basic-example-processor.yml
spring.initializr.name=DemoScsProcessor
spring.initializr.artifactId=DemoScsProcessor
spring.initializr.groupId=com.solace.test.processor
spring.initializr.version=0.0.1
spring.initializr.description=thisIsADescription
spring.initializr.packageName=com.solace.test.processor
spring.initializr.javaVersion=1.8
spring.initializr.language=java
spring.initializr.baseDir=SCSProcessorproject
spring.initializr.bootVersion=2.0.3.RELEASE
spring.initializr.type=maven-build
spring.initializr.packaging=jar
2) run the jar file 
The output ends up in the initializr directory relative to where you run it. 