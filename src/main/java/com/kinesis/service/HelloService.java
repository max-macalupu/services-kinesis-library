package com.kinesis.service;

import com.kinesis.record.StreamAlert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HelloService {

  private static final Logger log = LoggerFactory.getLogger(HelloService.class);

  public void takeCallAtIntegration(StreamAlert streamAlert) {
    log.info("Calling inside of integration, {}", streamAlert);
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}