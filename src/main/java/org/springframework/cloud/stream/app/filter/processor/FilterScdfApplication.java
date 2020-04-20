package org.springframework.cloud.stream.app.filter.processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
//import org.springframework.cloud.stream.app.filter.processor.FilterProcessorProperties;
import org.springframework.cloud.stream.config.BindingProperties;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.annotation.Filter;
import org.springframework.integration.support.MutableMessage;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import lombok.extern.slf4j.Slf4j;


@SpringBootApplication
@Slf4j
//@Import(FilterProcessorConfiguration.class)
@EnableBinding(Processor.class)
@EnableConfigurationProperties(FilterProcessorProperties.class)
public class FilterScdfApplication {

	public static void main(String[] args) {
		SpringApplication.run(FilterScdfApplication.class, args);
		
	}

	    @Autowired
	    private FilterProcessorProperties properties;
	    @Filter(inputChannel = Processor.INPUT, outputChannel = Processor.OUTPUT)
	    public boolean filter(Message<?> message) {
	        if (message.getPayload() instanceof byte[]) {
	            String contentType = message.getHeaders().containsKey(MessageHeaders.CONTENT_TYPE)
	                    ? message.getHeaders().get(MessageHeaders.CONTENT_TYPE).toString()
	                    : BindingProperties.DEFAULT_CONTENT_TYPE.toString();
	          if (contentType.contains("text") || contentType.contains("json") || contentType.contains("x-spring-tuple")) {
	                message = new MutableMessage<>(new String(((byte[]) message.getPayload())), message.getHeaders());
	                LOGGER.info("message :: " + message);
	            }
	        }
	        LOGGER.info("contentType :: " + (message.getHeaders().containsKey(MessageHeaders.CONTENT_TYPE)
                    ? message.getHeaders().get(MessageHeaders.CONTENT_TYPE).toString()
                    : BindingProperties.DEFAULT_CONTENT_TYPE.toString()));
	        LOGGER.info("message :: " + message);
	        LOGGER.info("withproperty :: " + this.properties.getExpression().getValue(message, Boolean.class));
	        return this.properties.getExpression().getValue(message, Boolean.class);
	        
	    }
}
