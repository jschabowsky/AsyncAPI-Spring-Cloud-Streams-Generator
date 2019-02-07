package com.solace.events.asyncapi.spring.cloud.streams.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
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
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.integration.annotation.InboundChannelAdapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.TypeExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.utils.CodeGenerationUtils;
import com.github.javaparser.utils.SourceRoot;

import de.dentrassi.asyncapi.AsyncApi;
import de.dentrassi.asyncapi.Topic;
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

@SpringBootApplication
@EnableConfigurationProperties(InitializrProperties.class)
@PropertySource("classpath:application.properties")
// @EnableAutoConfiguration
public class SpringCloudStreamsGenerator implements ApplicationEventPublisher {

	static final Dependency RABBIT_BINDER = Dependency.withId("cloud-stream-binder-rabbit", "org.springframework.cloud",
			"spring-cloud-stream-binder-rabbit");
	static final Dependency SCS_TEST = Dependency.withId("cloud-stream-test", "org.springframework.cloud",
			"spring-cloud-stream-test-support", null, Dependency.SCOPE_TEST);

	private AsyncApi asyncApiData = null;
	private Signature signature = null;

	public static void main(String[] args) {
		SpringApplication.run(SpringCloudStreamsGenerator.class, args);
	}

	@Autowired
	private InitializrProperties properties;

	@Autowired
	private SpringCloudStreamsGeneratorProperties scsGenProps;

	@Autowired
	private SCSProjectRequest scsProjectRequest;

	@PostConstruct
	public void generateSCS() throws Exception, IOException, ProcessingException, ParserException {
		// Validate Contract against asyncapi schema
		if (!this.validateContract(new File("src/main/resources/asyncapi.json"),
				new File("src/main/resources/oneof.yml"))) {
			return;
		}

		// Take Contract and parse into AsyncApi Java Object Model for
		// processing
		YamlParser yamlParser = new YamlParser(new FileInputStream(new File(scsGenProps.getAsyncAPIfile())));
		this.asyncApiData = yamlParser.parse();
		this.signature = extractSignature(asyncApiData);

		ProjectRequest projectRequest = scsProjectRequest.getProjectRequest();

		File out = generateInitilizrProject(projectRequest);

		String source = generateSourceCode(out, projectRequest);
		System.out.println(source);

		String yamlConfig = generateApplicationYaml(out, projectRequest);
		System.out.println(yamlConfig);
	}

	private void writeFile(File theFile, String text) throws Exception {
		FileOutputStream output = new FileOutputStream(theFile, false);
		output.write(text.getBytes());
		output.close();
	}

	private File generateInitilizrProject(ProjectRequest projectRequest) throws IOException {
		// Initial Spring Initializr Setup
		properties = load(new ClassPathResource("spring-bom.yml"));
		InitializrMetadata metadata = InitializrMetadataBuilder.fromInitializrProperties(properties).build();
		List<String> dependencies = new ArrayList<String>();

		dependencies.add("cloud-stream");
		dependencies.add("amqp");
		dependencies.add("rabbitmq-binder");
		projectRequest.setDependencies(dependencies);

		// Below does not work...
		// List<Dependency> resolvDeps = new ArrayList<Dependency>();
		// projectRequest.setResolvedDependencies(resolvDeps);
		// projectRequest.getResolvedDependencies().add(SpringCloudStreamsGenerator.RABBIT_BINDER);
		// projectRequest.getResolvedDependencies().add(SpringCloudStreamsGenerator.SCS_TEST);
		ProjectGenerator projectGenerator = new ProjectGenerator();
		projectGenerator.setMetadataProvider(new SimpleInitializrMetadataProvider(metadata));
		projectGenerator.setRequestResolver(new ProjectRequestResolver(new ArrayList<>()));
		projectGenerator.setEventPublisher(this);
		File out = projectGenerator.generateProjectStructure(projectRequest);
		System.out.println(out);
		final Builder builder = Generator.newBuilder();
		builder.targetPath(Paths.get(out.toString() + File.separator + scsGenProps.getBaseDir() + "/src/main/java"));
		builder.basePackage(scsGenProps.getPackageName());
		builder.build(asyncApiData).generate();

		return out;
	}

	private String generateSourceCode(File initilizrOutputDirectory, ProjectRequest projectRequest) throws Exception {
		// Add required source code based on contract
		SourceRoot sourceRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(SpringCloudStreamsGenerator.class)
				.resolve(initilizrOutputDirectory.getAbsolutePath() + File.separator + scsGenProps.getBaseDir()
						+ "/src/main/java"));
		CompilationUnit cu = sourceRoot.parse(projectRequest.getPackageName(), projectRequest.getName() + "Application.java");
		cu.addImport(scsGenProps.getPackageName() + ".types.*");
		cu.addImport(scsGenProps.getPackageName() + ".messages.*");
		ClassOrInterfaceDeclaration dec = cu.getClassByName(projectRequest.getApplicationName()).get();
		MethodDeclaration method = null;
		if (scsGenProps.getScsType().compareTo(SpringCloudStreamsGeneratorProperties.PROCESSOR) == 0) {
			method = dec.addMethod("handle", Modifier.PUBLIC);
			dec.addSingleMemberAnnotation(org.springframework.cloud.stream.annotation.EnableBinding.class,
					new ClassExpr(JavaParser.parseClassOrInterfaceType("Processor")));
			dec.tryAddImportToParentCompilationUnit(Processor.class);
			method.setType(signature.getPublishMessageType());
			method.addParameter(signature.getSubscribeMessageType(),
					"a" + signature.getSubscribeMessageType() + "Message");
			method.addSingleMemberAnnotation(org.springframework.cloud.stream.annotation.StreamListener.class,
					new TypeExpr(JavaParser.parseType("Processor.INPUT")));
			method.addSingleMemberAnnotation(org.springframework.cloud.stream.annotation.Output.class,
					new TypeExpr(JavaParser.parseType("Processor.OUTPUT")));

		} else if (scsGenProps.getScsType().compareTo(SpringCloudStreamsGeneratorProperties.SOURCE) == 0) {
			method = dec.addMethod("send" + signature.getPublishMessageType(), Modifier.PUBLIC);
			dec.addSingleMemberAnnotation(org.springframework.cloud.stream.annotation.EnableBinding.class,
					new ClassExpr(JavaParser.parseClassOrInterfaceType("Source")));
			dec.tryAddImportToParentCompilationUnit(Source.class);
			method.setType(signature.getPublishMessageType());
			method.addSingleMemberAnnotation(InboundChannelAdapter.class,
					new TypeExpr(JavaParser.parseType("Processor.OUTPUT")));
			dec.tryAddImportToParentCompilationUnit(Processor.class);
		} else if (scsGenProps.getScsType().compareTo(SpringCloudStreamsGeneratorProperties.SINK) == 0) {
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
		if (scsGenProps.getScsType().compareTo(SpringCloudStreamsGeneratorProperties.PROCESSOR) == 0
				|| scsGenProps.getScsType().compareTo(SpringCloudStreamsGeneratorProperties.SINK) == 0) {
			BindingProperties bpSubscribe = new BindingProperties();
			bpSubscribe.setBinder("local_rabbit");
			bpSubscribe.setDestination(signature.getSubscribeTopic());
			bindingMap.put("input", bpSubscribe);
		}
		if (scsGenProps.getScsType().compareTo(SpringCloudStreamsGeneratorProperties.PROCESSOR) == 0
				|| scsGenProps.getScsType().compareTo(SpringCloudStreamsGeneratorProperties.SOURCE) == 0) {
			BindingProperties bpPublish = new BindingProperties();
			bpPublish.setBinder("local_rabbit");
			bpPublish.setDestination(signature.getPublishTopic());
			bindingMap.put("output", bpPublish);
		}
		bsp.setBindings(bindingMap);
		BinderProperties binderprops = new BinderProperties();
		binderprops.setType("rabbit");
		Map<String, Object> environment = new HashMap<String, Object>();
		Map<String, Object> rabbitEnv = new HashMap<String, Object>();
		environment.put("spring", rabbitEnv);
		String[] hostSplit = asyncApiData.getHost().split(":");
		rabbitEnv.put("host", hostSplit[0]);
		rabbitEnv.put("port", hostSplit[1]);
		rabbitEnv.put("username", "guest");
		rabbitEnv.put("password", "guest");
		rabbitEnv.put("virtual-host", "/");

		binderprops.setEnvironment(environment);
		HashMap<String, BinderProperties> config = new HashMap<String, BinderProperties>();
		config.put("local_rabbit", binderprops);
		bsp.setBinders(config);

		ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
		ObjectNode file = (ObjectNode) mapper.createObjectNode().set("spring", mapper.createObjectNode().set("cloud",
				mapper.createObjectNode().set("stream", mapper.convertValue(bsp, ObjectNode.class))));
		String output = mapper.writeValueAsString(file);
		SourceRoot sourceRoot = new SourceRoot(CodeGenerationUtils.mavenModuleRoot(SpringCloudStreamsGenerator.class)
				.resolve(initilizrOutputDirectory.getAbsolutePath() + File.separator + scsGenProps.getBaseDir()
						+ "/src/main/resources"));
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
		Iterator<Topic> topicIter = api.getTopics().iterator();
		while (topicIter.hasNext()) {
			Topic topic = topicIter.next();
			if (topic.getPublish() != null) {
				signature.setPublishTopic(api.getBaseTopic() + "." + topic.getName());
				String className = topic.getPublish().getName();
				signature.setPublishMessageType(className.substring(0, 1).toUpperCase() + className.substring(1));
			} else if (topic.getSubscribe() != null) {
				signature.setSubscribeTopic(api.getBaseTopic() + "." + topic.getName());
				String className = topic.getSubscribe().getName();
				signature.setSubscribeMessageType(className.substring(0, 1).toUpperCase() + className.substring(1));

			}
		}

		return signature;
	}

	private boolean validateContract(File asyncApiSchema, File asyncApiInstance) throws Exception {
		ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
		final JsonNode asyncApiInstanceModel = yamlReader.readValue(asyncApiInstance, JsonNode.class);
		final JsonSchema schema = JsonSchemaFactory.byDefault().getJsonSchema(JsonLoader.fromFile(asyncApiSchema));
		ProcessingReport report;
		report = schema.validate(asyncApiInstanceModel);
		if (report.isSuccess()) {
			return true;
		} else {
			System.out.println(report);
			throw new Exception(report.toString());
		}

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
