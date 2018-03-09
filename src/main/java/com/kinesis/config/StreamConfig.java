package com.kinesis.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorFactory;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;
import com.kinesis.processor.RecordProcesor;
import com.kinesis.processor.RecordProcesorFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class StreamConfig {


    @Bean
    public RecordProcesor getProcessRecord() {
        return new RecordProcesor();
    }

    @Bean
    @Primary
    public IRecordProcessorFactory getIRecordProcessorFactory(RecordProcesor recordProcesor) {
        return new RecordProcesorFactory(recordProcesor);
    }

    @Bean
    public KinesisClientLibConfiguration getKinesisLibConfiguration(AWSCredentialsProvider credentialsProvider) {
        return new KinesisClientLibConfiguration(
                "KinesisProducerLibSampleConsumer",
                "Test-Alerts-Kinesis",
                credentialsProvider,
                "KinesisProducerLibSampleConsumer"
                ).withRegionName("us-east-1")
                .withInitialPositionInStream(InitialPositionInStream.TRIM_HORIZON);
    }

    @Bean
    public Worker getWorker(IRecordProcessorFactory iRecordFactory, KinesisClientLibConfiguration libConfiguration) {
        return new Worker
                .Builder()
                .config(libConfiguration)
                .recordProcessorFactory(iRecordFactory)
                .build();
    }

//    @Bean
//    public TaskExecutor threadPoolTaskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(4);
//        executor.setMaxPoolSize(4);
//        executor.setQueueCapacity(10);
//        executor.setThreadNamePrefix("default_task_executor_thread");
//        executor.initialize();
//        return executor;
//    }
}