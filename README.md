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
1) mvn clean install
2) generated jar file is runnable like so:
java -jar AsyncAPI-SpringCloudStreams-Generator-0.0.1-SNAPSHOT-spring-boot.jar --p="com.solace.spring.cloud.streams.test" --java --jar --mvn --sbv=2.1.4.RELEASE --jv=1.8 --cu=solace-cloud-client --cp=XXXXXXXXXXXXXXXX --mvpn=msgvpn No-Yelling-Processor.yaml

Options are:
-p source code root package
--java means to use java
--jar means to package as a jar
--mvn means to make it a maven build
--sbv is the spring boot version to use 
--jv is the java version to use 
--cu is the solace client username
--cp is the solace client password
--mvpn is the solace message vpn to connect to 
the last argument is the AsyncAPI file (2.0.0-rc1) to generate into code. 
