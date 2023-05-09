package yang.plane.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.annotation.IntegrationComponentScan;
import tk.mybatis.spring.annotation.MapperScan;
import yang.plane.mapper.my.mapper.MyMapper;

@SpringBootApplication
@IntegrationComponentScan(basePackages = {"yang.plane.*"})
@MapperScan(basePackages = {"yang.plane.mapper"},markerInterface = MyMapper.class)
@ComponentScan(basePackages = {"yang.plane.*"})
public class PlaneApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlaneApplication.class);
    }
}
