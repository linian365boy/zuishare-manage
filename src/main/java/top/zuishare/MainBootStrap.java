package top.zuishare;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import top.zuishare.util.ScheduleUtil;

@Configuration
@ComponentScan(basePackages={"top.zuishare"})
@MapperScan(basePackages={"top.zuishare.dao"})
@EnableAutoConfiguration
public class MainBootStrap extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MainBootStrap.class);
	}
	
	public static void main(String[] args) {
        SpringApplication.run(MainBootStrap.class, args);
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				//关闭资源
				ScheduleUtil.closeResource();
			}
		});
    }
}
