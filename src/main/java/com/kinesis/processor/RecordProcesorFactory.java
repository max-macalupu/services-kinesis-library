package com.kinesis.processor;

import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RecordProcesorFactory implements IRecordProcessorFactory {
    private RecordProcesor recordProcesor;

    public RecordProcesorFactory(RecordProcesor recordProcesor) {
        this.recordProcesor = recordProcesor;
    }

    @Override
    public IRecordProcessor createProcessor() {
        return recordProcesor;
    }
}
