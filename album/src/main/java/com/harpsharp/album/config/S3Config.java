package com.harpsharp.album.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {

    @Bean
    @Profile("local")
    public S3Client localS3Client(){
        return S3Client.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(ProfileCredentialsProvider.create("default"))
                .build();
    }

    @Bean
    @Profile("prod")
    public S3Client awsS3Client(){
        return S3Client.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(InstanceProfileCredentialsProvider.create())
                .build();
    }

    @Bean
    @Profile("local")
    public S3Presigner localPresigner(){
        return S3Presigner
                .builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(ProfileCredentialsProvider.create("default"))
                .build();
    }

    @Bean
    @Profile("prod")
    public S3Presigner presigner(){
        return S3Presigner
                .builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(InstanceProfileCredentialsProvider.create())
                .build();
    }

}

