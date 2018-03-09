package com.kinesis.processor;

import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorCheckpointer;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.ShutdownReason;
import com.amazonaws.services.kinesis.model.Record;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kinesis.record.StreamAlert;
import com.kinesis.service.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.rmi.CORBA.ValueHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class RecordProcesor implements IRecordProcessor {

    private static final Logger log = LoggerFactory.getLogger(RecordProcesor.class);

    private final Object lock = new Object();
    private final AtomicLong largestTimestamp = new AtomicLong(0);
    private final List<Long> sequenceNumbers = new ArrayList<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private HelloService helloService;


    @Override
    public void initialize(String s) {

    }

    @Override
    public void processRecords(List<Record> list, IRecordProcessorCheckpointer checkpointer) {
        long timestamp = 0;
        List<Long> seqNos = new ArrayList<>();

        for (Record r : list) {
            // Get the timestamp of this run from the partition key.
            timestamp = Math.max(timestamp, Long.parseLong(r.getPartitionKey()));

            // Extract the sequence number. It's encoded as a decimal
            // string and placed at the beginning of the record data,
            // followed by a space. The rest of the record data is padding
            // that we will simply discard.
            try {
                byte[] b = new byte[r.getData().remaining()];
                r.getData().get(b);
                String data = new String(b, "UTF-8");
//                String data = new String(b, "UTF-8").split(" ")[0];
                StreamAlert streamAlert = objectMapper
                    .readValue(data, StreamAlert.class);
//                System.out.println(streamAlert.toString());
//                System.out.println(data);
                helloService.takeCallAtIntegration(streamAlert);
            } catch (Exception e) {
                log.error("Error parsing record", e);
                System.exit(1);
            }
        }

        synchronized (lock) {
            if (largestTimestamp.get() < timestamp) {
                log.info(String.format(
                    "Found new larger timestamp: %d (was %d), clearing state",
                    timestamp, largestTimestamp.get()));
                largestTimestamp.set(timestamp);
                sequenceNumbers.clear();
            }

            // Only add to the shared list if our data is from the latest run.
            if (largestTimestamp.get() == timestamp) {
                sequenceNumbers.addAll(seqNos);
                Collections.sort(sequenceNumbers);
            }
        }

        try {
            checkpointer.checkpoint();
        } catch (Exception e) {
            log.error("Error while trying to checkpoint during ProcessRecords", e);
        }
    }

    @Override
    public void shutdown(IRecordProcessorCheckpointer checkpointer, ShutdownReason shutdownReason) {
        log.info("Shutting down, reason: " + shutdownReason);
            try {
                checkpointer.checkpoint();
            } catch (Exception e) {
                log.error("Error while trying to checkpoint during Shutdown", e);
            }
    }
}

