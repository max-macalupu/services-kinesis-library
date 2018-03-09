package com.kinesis.component;

import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class HelloComponent implements ApplicationRunner {

  @Autowired
  private Worker worker;

//  @Async
  @Override
  public void run(ApplicationArguments applicationArguments) throws Exception {
    System.out.println("printing inside of ApplicationRunner");
    worker.run();
    System.out.println("Runing daemon of kinesis");
//    while (true) {
//      Thread.sleep(1000);
//      System.out.println("Iteration as daemon...!");
//    }
  }
}
