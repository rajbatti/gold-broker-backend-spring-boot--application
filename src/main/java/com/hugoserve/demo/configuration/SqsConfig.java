package com.hugoserve.demo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class SqsConfig {
    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;
    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;
    @Value("${cloud.aws.credentials.sessionToken}")
    private String sessionToken;

    @Bean
    public SqsClient sqsClient() {
        AwsSessionCredentials temporaryCredentials = AwsSessionCredentials.create(
                accessKey, secretKey, sessionToken);


        return SqsClient.builder()
                .region(Region.AP_SOUTH_1) // Set your region
                .credentialsProvider(StaticCredentialsProvider.create(temporaryCredentials))
                .build();
    }
}


