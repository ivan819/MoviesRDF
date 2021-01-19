package com.ivan.MoviesRDF;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan(basePackages = "com.ivan")
@PropertySource("classpath:application.properties")
public class Config {
    @Autowired
    private Environment env;

    @Value("${spark.app.name}")
    private String appName;

    @Value("${spark.home}")
    private String sparkHome;

    @Value("${spark.master.uri}")
    private String masterUri;

    @Bean
    public SparkConf sparkConf() {
        SparkConf sparkConf = new SparkConf().setAppName(appName).setSparkHome(sparkHome).setMaster(masterUri);

        return sparkConf;
    }

    @Bean
    public JavaSparkContext javaSparkContext() {
        JavaSparkContext ctx = new JavaSparkContext(sparkConf());
        // ctx.text(path)
        return ctx;
    }

    @Bean
    public SparkSession sparkSession() {
        javaSparkContext().sc().uiWebUrl().get();
        return SparkSession.builder().sparkContext(javaSparkContext().sc()).appName("Java Spark SQL basic example")
                .getOrCreate();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}