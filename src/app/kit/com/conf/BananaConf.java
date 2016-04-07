package app.kit.com.conf;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

/**
 * Configuration class
 * @author jman
 *
 */
@Configuration
@Component
@Qualifier("bananaConf")
@ComponentScan("app.kit, app.kit.controller, app.kit.service, app.kit.vo, app.kit.handler")
@ImportResource("classpath:/conf/ApplicationContext.xml")
@EnableScheduling
public class BananaConf {

}
