package nel.marco.config.springwebflow;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.webflow.config.FlowBuilderServicesBuilder;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;
import org.springframework.webflow.mvc.builder.MvcViewFactoryCreator;
import org.springframework.webflow.mvc.servlet.FlowHandlerAdapter;
import org.springframework.webflow.mvc.servlet.FlowHandlerMapping;

import java.util.Collections;

@Configuration
public class WebFlowAndMvcConfig {

  private final WebMvcConfig webMvcConfig;
  private final WebFlowConfig webFlowConfig;

  public WebFlowAndMvcConfig(WebMvcConfig webMvcConfig, WebFlowConfig webFlowConfig) {
    this.webMvcConfig = webMvcConfig;
    this.webFlowConfig = webFlowConfig;
  }

  @Bean
  public FlowHandlerMapping flowHandlerMapping() {
    FlowHandlerMapping handlerMapping = new FlowHandlerMapping();
    handlerMapping.setOrder(-1);
    handlerMapping.setFlowRegistry(this.webFlowConfig.flowRegistry());
    return handlerMapping;
  }

  @Bean
  public FlowHandlerAdapter flowHandlerAdapter() {
    FlowHandlerAdapter handlerAdapter = new FlowHandlerAdapter();
    handlerAdapter.setFlowExecutor(this.webFlowConfig.flowExecutor());
    handlerAdapter.setSaveOutputToFlashScopeOnRedirect(true);
    return handlerAdapter;
  }

  @Bean
  public MvcViewFactoryCreator mvcViewFactoryCreator() {
    MvcViewFactoryCreator factoryCreator = new MvcViewFactoryCreator();
    factoryCreator.setViewResolvers(Collections.singletonList(this.webMvcConfig.viewResolver()));
    factoryCreator.setUseSpringBeanBinding(true);
    return factoryCreator;
  }

  @Bean
  public FlowBuilderServices flowBuilderServices() {
    return new FlowBuilderServicesBuilder()
        .setViewFactoryCreator(mvcViewFactoryCreator())
        .setDevelopmentMode(true)
        .build();
  }
}
