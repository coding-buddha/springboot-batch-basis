package edu.pasudo123.study.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringbootBatchBasisApplication {

    public static void main(String[] args) {
        final ConfigurableApplicationContext context = SpringApplication.run(SpringbootBatchBasisApplication.class, args);
        System.exit(SpringApplication.exit(context));
    }

}
