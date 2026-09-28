package br.gov.es.pmo.indicator_parser.config;

import br.gov.es.pmo.indicator_parser.properties.PentahoIndicatorProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("br.gov.es.pmo.indicator_parser")
@EnableConfigurationProperties(PentahoIndicatorProperties.class)
@PropertySource("classpath:indicator-parser.properties")
public class IndicatorParserAutoConfig {
}
