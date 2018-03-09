package com.kinesis.config;

import com.amazonaws.auth.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsAccess {

  @Bean
  @Qualifier("kinesisKeys")
  public AWSCredentials getCredentials() {
    return new BasicAWSCredentials("AKIAIJQXU5LHOODYYS6A",
        "dUqvgcDFN3j6Hs7kiMjnwvihaYWrdgU/c8iAtBAk");
  }

  @Bean
  public AWSCredentialsProvider awsCredentialsProvider(@Qualifier("kinesisKeys") AWSCredentials awsCredentials) {
    return new DefaultAWSCredentialsProviderChain();
//    return new AWSStaticCredentialsProvider(awsCredentials);
  }

}
