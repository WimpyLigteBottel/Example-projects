package nel.marco.config.springwebflow;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.webflow.config.AbstractFlowConfiguration;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.executor.FlowExecutor;

@Configuration
public class WebFlowConfig extends AbstractFlowConfiguration {

  @Bean
  public FlowDefinitionRegistry flowRegistry() {
    return getFlowDefinitionRegistryBuilder()
        .addFlowLocationPattern("/WEB-INF/flows/*Flow.xml")
        .build();
  }

  @Bean
  public FlowExecutor flowExecutor() {
    return getFlowExecutorBuilder(flowRegistry()).build();
  }
}
