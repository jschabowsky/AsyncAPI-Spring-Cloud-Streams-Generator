package com.solace.events.asyncapi.spring.cloud.streams.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.cloud.stream.config.BinderProperties;
import org.springframework.cloud.stream.config.BindingProperties;
import org.springframework.cloud.stream.config.BindingServiceProperties;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.integration.annotation.InboundChannelAdapter;

import com.asyncapi.parser.Channel;
import com.asyncapi.parser.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.TypeExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.utils.SourceRoot;

import de.dentrassi.asyncapi.AsyncApi;
import de.dentrassi.asyncapi.generator.java.Generator;
import de.dentrassi.asyncapi.generator.java.Generator.Builder;
import de.dentrassi.asyncapi.internal.parser.ParserException;
import de.dentrassi.asyncapi.internal.parser.YamlParser;
import io.spring.initializr.generator.ProjectGenerator;
import io.spring.initializr.generator.ProjectRequest;
import io.spring.initializr.generator.ProjectRequestResolver;
import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.metadata.InitializrMetadata;
import io.spring.initializr.metadata.InitializrMetadataBuilder;
import io.spring.initializr.metadata.InitializrProperties;
import io.spring.initializr.metadata.SimpleInitializrMetadataProvider;
import reactor.core.publisher.Flux;

@SpringBootApplication
@EnableConfigurationProperties(InitializrProperties.class)
@PropertySource("classpath:application.properties")
// @EnableAutoConfiguration
public class SpringCloudStreamsGenerator implements ApplicationRunner, ApplicationEventPublisher {

	static final Dependency SCS_TEST = Dependency.withId("cloud-stream-test", "org.springframework.cloud",
			"spring-cloud-stream-test-support", null, Dependency.SCOPE_TEST);

	private AsyncApi asyncApiData = null;
	private Signature signature = null;

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudStreamsGenerator.class, args);

	}

	@Autowired
	private InitializrProperties initializrProps;

	@Autowired
	private SpringCloudStreamsGeneratorProperties scsGenProps;

	@Autowired
	private SCSProjectRequest scsProjectRequest;

	public void run(ApplicationArguments args) throws Exception, IOException, ProcessingException, ParserException {

		if (!args.containsOption("p") || !args.containsOption("cu") || !args.containsOption("cp")
				|| !args.containsOption("mvpn") || args.getNonOptionArgs().size() != 1) {
			System.out.println("USAGE: ");
			System.out.println(
					"--p=<package> --cu=<client-username> --cp=<client-password> --mvpn=<message-vpn> <asyncapi-file-path>");
			System.exit(-1);
		}

		if (args.containsOption("p")) {
			scsProjectRequest.setPackageName(args.getOptionValues("p").get(0));
		}
		if (args.containsOption("sbv")) {
			scsProjectRequest.setBootVersion(args.getOptionValues("sbv").get(0));
		}
		if (args.containsOption("jv")) {
			scsProjectRequest.setJavaVersion(args.getOptionValues("jv").get(0));
		}
		if (args.containsOption("java")) {
			scsProjectRequest.setLanguage("java");
		}
		if (args.containsOption("jar")) {
			scsProjectRequest.setPackaging("jar");
		}
		if (args.containsOption("mvn")) {
			scsProjectRequest.setType("maven-build");
		}
		if (args.containsOption("cu")) {
			scsGenProps.setClientUsername(args.getOptionValues("cu").get(0));
			;
		}
		if (args.containsOption("cp")) {
			scsGenProps.setClientPassword(args.getOptionValues("cp").get(0));
			;
		}
		if (args.containsOption("mvpn")) {
			scsGenProps.setMsgVpn(args.getOptionValues("mvpn").get(0));
			;
		}
		if (args.containsOption("reactive")) {
			scsGenProps.setReactive(true);
		}

		// Take AsyncAPI Contract and parse into a signature object
		YamlParser yamlParser = new YamlParser(new FileInputStream(new File(args.getNonOptionArgs().get(0))));
		this.asyncApiData = yamlParser.parse();
		this.signature = extractSignature(asyncApiData);

		// Generate Spring Project though Spring Initializer based on properties
		if (scsProjectRequest.getBaseDir() == null) {
			scsProjectRequest.setBaseDir(asyncApiData.getInfo().getTitle());
		}
		if (scsProjectRequest.getName() == null) {
			scsProjectRequest.setName(asyncApiData.getInfo().getTitle());
		}
		if (scsProjectRequest.getArtifactId() == null) {
			scsProjectRequest.setArtifactId(asyncApiData.getInfo().getTitle());
		}
		if (scsProjectRequest.getGroupId() == null) {
			scsProjectRequest.setGroupId(scsProjectRequest.getPackageName());
		}
		if (scsProjectRequest.getVersion() == null) {
			scsProjectRequest.setVersion(asyncApiData.getInfo().getVersion());
		}
		if (scsProjectRequest.getDescription() == null) {
			scsProjectRequest.setDescription("");
		}
		ProjectRequest projectRequest = scsProjectRequest.getProjectRequest();
		File out = generateInitilizrProject(projectRequest);
		// Generate Java Objects based off of publish/subscribe models defined
		// in the Contract
		generateObjectModel(out);

		// Generate Spring Cloud Streams Java Object with Annotations and
		// Methods
		String source = generateSourceCode(out, projectRequest, scsGenProps);
		System.out.println(source);

		// Generate Application.Yaml file which links the SCS Bindings to the
		// Solace Binder
		String yamlConfig = generateApplicationYaml(out, projectRequest);
		System.out.println(yamlConfig);

		System.out.println("Your Project Has Been Generated at: " + out);

	}

	private void writeFile(File theFile, String text) throws Exception {
		FileOutputStream output = new FileOutputStream(theFile, false);
		output.write(text.getBytes());
		output.close();
	}

	private void generateObjectModel(File out) throws IOException {
		final Builder builder = Generator.newBuilder();
		builder.targetPath(
				Paths.get(out.toString() + File.separator + scsProjectRequest.getBaseDir() + "/src/main/java"));
		builder.basePackage(scsProjectRequest.getPackageName());
		builder.build(asyncApiData).generate();

	}

	private File generateInitilizrProject(ProjectRequest projectRequest) throws IOException {
		// Initial Spring Initializr Setup

		initializrProps = load(new ClassPathResource("spring-bom.yml"));
		InitializrMetadata metadata = InitializrMetadataBuilder.fromInitializrProperties(initializrProps).build();
		List<String> dependencies = new ArrayList<String>();

		dependencies.add("cloud-stream");
		dependencies.add("web");
		dependencies.add("cloud-stream-binder-solace");
		projectRequest.setDependencies(dependencies);

		ProjectGenerator projectGenerator = new ProjectGenerator();
		projectGenerator.setMetadataProvider(new SimpleInitializrMetadataProvider(metadata));
		projectGenerator.setRequestResolver(new ProjectRequestResolver(new ArrayList<>()));
		projectGenerator.setEventPublisher(this);
		File out = projectGenerator.generateProjectStructure(projectRequest);

		return out;
	}

	private String generateSourceCode(File initilizrOutputDirectory, ProjectRequest projectRequest,
			SpringCloudStreamsGeneratorProperties scsGenProps) throws Exception {
		// Add required source code based on contract

		Path path = Paths
				.get(initilizrOutputDirectory.getAbsolutePath() + File.separator + scsProjectRequest.getBaseDir()
						+ File.separator + "src" + File.separator + "main" + File.separator + "java");
		SourceRoot sourceRoot = new SourceRoot(path);
		CompilationUnit cu = sourceRoot.parse(projectRequest.getPackageName(),
				projectRequest.getName() + "Application.java");
		cu.addImport(scsProjectRequest.getPackageName() + ".types.*");
		cu.addImport(scsProjectRequest.getPackageName() + ".messages.*");
		ClassOrInterfaceDeclaration dec = cu.getClassByName(projectRequest.getApplicationName()).get();
		MethodDeclaration method = null;

		if (scsGenProps.isReactive()) {
			dec.tryAddImportToParentCompilationUnit(Flux.class);
			if (signature.getPublishMessageType() != null && signature.getSubscribeMessageType() != null) {
				method = dec.addMethod("handle" + signature.getSubscribeMessageType(), Modifier.PUBLIC);
				method.addMarkerAnnotation(Bean.class);
				dec.addSingleMemberAnnotation(org.springframework.cloud.stream.annotation.EnableBinding.class,
						new ClassExpr(JavaParser.parseClassOrInterfaceType("Processor")));
				dec.tryAddImportToParentCompilationUnit(Function.class);

				String publishType = null;
				String subscribeType = null;
				for (Message m : asyncApiData.getMessages()) {
					if (m.getName().compareTo(signature.getPublishMessageType()) == 0) {
						publishType = m.getPayload().getName();
						break;
					}
				}
				for (Message m : asyncApiData.getMessages()) {
					if (m.getName().compareTo(signature.getSubscribeMessageType()) == 0) {
						subscribeType = m.getPayload().getName();
						method.setType("Function<Flux<" + subscribeType + ">, Flux<" + publishType + ">>");
						break;
					}
				}
				BlockStmt body =  dec.getMethodsByName("main").get(0).getBody().get();
			

			} else if (signature.getPublishMessageType() != null) {
				method = dec.addMethod("send" + signature.getPublishMessageType(), Modifier.PUBLIC);
				method.addMarkerAnnotation(Bean.class);
				dec.tryAddImportToParentCompilationUnit(Supplier.class);
				method.setType("Supplier<Flux<" + signature.getPublishMessageType() + ">>");
			} else if (signature.getSubscribeMessageType() != null) {
				method = dec.addMethod("consume" + signature.getSubscribeMessageType(), Modifier.PUBLIC);
				method.addMarkerAnnotation(Bean.class);
				dec.tryAddImportToParentCompilationUnit(Consumer.class);
				method.setType("Consumer<Flux<" + signature.getSubscribeMessageType() + ">>");
			}
		} else {
			if (signature.getPublishMessageType() != null && signature.getSubscribeMessageType() != null) {
				method = dec.addMethod("handle" + signature.getSubscribeMessageType(), Modifier.PUBLIC);
				dec.addSingleMemberAnnotation(org.springframework.cloud.stream.annotation.EnableBinding.class,
						new ClassExpr(JavaParser.parseClassOrInterfaceType("Processor")));
				dec.tryAddImportToParentCompilationUnit(Processor.class);

				for (Message m : asyncApiData.getMessages()) {
					if (m.getName().compareTo(signature.getPublishMessageType()) == 0) {
						method.setType(m.getPayload().getName());
						break;
					}
				}
				for (Message m : asyncApiData.getMessages()) {
					if (m.getName().compareTo(signature.getSubscribeMessageType()) == 0) {
						method.addParameter(m.getPayload().getName(), "a" + m.getPayload().getName());
						break;
					}
				}

				method.addSingleMemberAnnotation(org.springframework.cloud.stream.annotation.StreamListener.class,
						new TypeExpr(JavaParser.parseType("Processor.INPUT")));
				method.addSingleMemberAnnotation(org.springframework.cloud.stream.annotation.Output.class,
						new TypeExpr(JavaParser.parseType("Processor.OUTPUT")));

			} else if (signature.getPublishMessageType() != null) {
				method = dec.addMethod("send" + signature.getPublishMessageType(), Modifier.PUBLIC);
				dec.addSingleMemberAnnotation(org.springframework.cloud.stream.annotation.EnableBinding.class,
						new ClassExpr(JavaParser.parseClassOrInterfaceType("Source")));
				dec.tryAddImportToParentCompilationUnit(Source.class);
				method.setType(signature.getPublishMessageType());
				method.addSingleMemberAnnotation(InboundChannelAdapter.class,
						new TypeExpr(JavaParser.parseType("Processor.OUTPUT")));
				dec.tryAddImportToParentCompilationUnit(Processor.class);
			} else if (signature.getSubscribeMessageType() != null) {
				method = dec.addMethod("consume" + signature.getSubscribeMessageType(), Modifier.PUBLIC);
				dec.addSingleMemberAnnotation(org.springframework.cloud.stream.annotation.EnableBinding.class,
						new ClassExpr(JavaParser.parseClassOrInterfaceType("Sink")));
				dec.tryAddImportToParentCompilationUnit(Sink.class);
				method.addParameter(signature.getSubscribeMessageType(),
						"a" + signature.getSubscribeMessageType() + "Message");
				method.addSingleMemberAnnotation(org.springframework.cloud.stream.annotation.StreamListener.class,
						new TypeExpr(JavaParser.parseType("Processor.INPUT")));
				dec.tryAddImportToParentCompilationUnit(Processor.class);

			}
		}

		BlockStmt body = method.createBody();
		body.addOrphanComment(new LineComment("Add Business Logic Here."));
		writeFile(new File(sourceRoot.getRoot().toString() + File.separator
				+ projectRequest.getPackageName().replaceAll("\\.", Matcher.quoteReplacement(File.separator))
				+ File.separator + projectRequest.getName() + "Application.java"), cu.toString());
		return cu.toString();
	}

	private String generateApplicationYaml(File initilizrOutputDirectory, ProjectRequest projectRequest)
			throws Exception {
		BindingServiceProperties bsp = new BindingServiceProperties();
		Map<String, BindingProperties> bindingMap = new HashMap<String, BindingProperties>();
		if (signature.getSubscribeMessageType() != null) {
			BindingProperties bpSubscribe = new BindingProperties();
			bpSubscribe.setBinder("solace");
			bpSubscribe.setDestination(signature.getSubscribeTopic());
			bindingMap.put("input", bpSubscribe);
		}
		if (signature.getPublishMessageType() != null) {
			BindingProperties bpPublish = new BindingProperties();
			bpPublish.setBinder("solace");
			bpPublish.setDestination(signature.getPublishTopic());
			bindingMap.put("output", bpPublish);
		}
		bsp.setBindings(bindingMap);
		BinderProperties binderprops = new BinderProperties();
		binderprops.setType("solace");
		Map<String, Object> environment = new HashMap<String, Object>();
		Map<String, Object> solaceEnv = new HashMap<String, Object>();
		Map<String, Object> javaEnv = new HashMap<String, Object>();
		environment.put("solace", solaceEnv);
		solaceEnv.put("java", javaEnv);
		javaEnv.put("host", asyncApiData.getServers().iterator().next().getUrl());
		javaEnv.put("clientUsername", scsGenProps.getClientUsername());
		javaEnv.put("clientPassword", scsGenProps.getClientPassword());
		javaEnv.put("msgVpn", scsGenProps.getMsgVpn());
		javaEnv.put("connectRetries", 3);
		javaEnv.put("connectRetriesPerHost", 0);
		javaEnv.put("reconnectRetries", 3);

		binderprops.setEnvironment(environment);
		HashMap<String, BinderProperties> config = new HashMap<String, BinderProperties>();
		config.put("solace", binderprops);
		bsp.setBinders(config);

		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		ObjectNode file = (ObjectNode) mapper.createObjectNode().set("spring", mapper.createObjectNode().set("cloud",
				mapper.createObjectNode().set("stream", mapper.convertValue(bsp, ObjectNode.class))));
		String output = mapper.writeValueAsString(file);

		Path path = Paths
				.get(initilizrOutputDirectory.getAbsolutePath() + File.separator + scsProjectRequest.getBaseDir()
						+ File.separator + "src" + File.separator + "main" + File.separator + "resources");
		SourceRoot sourceRoot = new SourceRoot(path);
		writeFile(new File(sourceRoot.getRoot().toString() + File.separator + "application.yml"), output);
		return output;
	}

	private static InitializrProperties load(Resource resource) {
		ConfigurationPropertySource source = new MapConfigurationPropertySource(loadProperties(resource));
		Binder binder = new Binder(source);
		return binder.bind("initializr", InitializrProperties.class).get();
	}

	private static Properties loadProperties(Resource resource) {
		YamlPropertiesFactoryBean yamlFactory = new YamlPropertiesFactoryBean();
		yamlFactory.setResources(resource);
		yamlFactory.afterPropertiesSet();
		return yamlFactory.getObject();
	}

	private Signature extractSignature(AsyncApi api) {
		Signature signature = new Signature();
		Iterator<Channel> topicIter = api.getChannel().iterator();
		while (topicIter.hasNext()) {
			Channel topic = topicIter.next();
			if (topic.getPublish() != null) {
				signature.setPublishTopic(topic.getChannel());
				String className = topic.getPublish().getName();
				signature.setPublishMessageType(className.substring(0, 1).toUpperCase() + className.substring(1));
			} else if (topic.getSubscribe() != null) {
				signature.setSubscribeTopic(topic.getChannel());
				String className = topic.getSubscribe().getName();
				signature.setSubscribeMessageType(className.substring(0, 1).toUpperCase() + className.substring(1));

			}
		}

		return signature;
	}

	@Override
	public void publishEvent(Object arg0) {
		// TODO Auto-generated method stub

	}

	public class Signature {
		public String getPublishTopic() {
			return publishTopic;
		}

		public void setPublishTopic(String publishTopic) {
			this.publishTopic = publishTopic;
		}

		public String getPublishMessageType() {
			return publishMessageType;
		}

		public void setPublishMessageType(String publishMessageType) {
			this.publishMessageType = publishMessageType;
		}

		public String getSubscribeTopic() {
			return subscribeTopic;
		}

		public void setSubscribeTopic(String subscribeTopic) {
			this.subscribeTopic = subscribeTopic;
		}

		public String getSubscribeMessageType() {
			return subscribeMessageType;
		}

		public void setSubscribeMessageType(String subscribeMessageType) {
			this.subscribeMessageType = subscribeMessageType;
		}

		private String publishTopic = null;
		private String publishMessageType = null;
		private String subscribeTopic = null;
		private String subscribeMessageType = null;
	}
}
