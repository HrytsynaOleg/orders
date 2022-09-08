package com.atlantis.orders.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;

@Configuration
//@EnableDynamoDBRepositories
//        (basePackages = "com.example.demo.repository")
public class DynamoDbConfig {
//    @Value("${amazon.dynamodb.endpoint}")
//    private String amazonDynamoDBEndpoint;
//
//    @Value("${amazon.aws.accesskey}")
//    private String amazonAWSAccessKey;
//
//    @Value("${amazon.aws.secretkey}")
//    private String amazonAWSSecretKey;
//
//    @Bean
//    public AmazonDynamoDB amazonDynamoDB() {
//        AmazonDynamoDB amazonDynamoDB
//                = new AmazonDynamoDBClient(amazonBasicAWSCredentials());
//
//        if (!amazonDynamoDBEndpoint.isEmpty()) {
//            amazonDynamoDB.setEndpoint(amazonDynamoDBEndpoint);
//        }
//
//        return amazonDynamoDB;
//    }
//
//    @Bean
//    public AWSCredentials amazonBasicAWSCredentials() {
//        return new BasicAWSCredentials(
//                amazonAWSAccessKey, amazonAWSSecretKey);
//    }

//    @Bean
//    public AwsCredentials amazonAwsCredentials() {
//        return new AwsCredentials() {
//
//            @Override
//            public String accessKeyId() {
//                return amazonAWSAccessKey;
//            }
//
//            @Override
//            public String secretAccessKey() {
//                return amazonAWSSecretKey;
//            }
//        };
//    }

    @Bean
    public DynamoDbClient getDynamoDbClient() {
//        AwsCredentialsProvider credentialsProvider =
//                StaticCredentialsProvider.create(amazonAwsCredentials());

//        return DynamoDbClient.builder()
//                .region(Region.EU_CENTRAL_1)
//                .credentialsProvider(credentialsProvider).build();

//        AwsCredentialsProvider credentialsProvider =
//                new EnvironmentVariableCredentialsProvider();

        return DynamoDbClient.builder()
                .region(Region.EU_CENTRAL_1)
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient getDynamoDbEnhancedClient() {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(getDynamoDbClient())
                .build();
    }
}
