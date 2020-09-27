package com.qcxk;

import com.qcxk.component.netty.DiscardServer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ApplicationRunnerImpl  implements ApplicationRunner {
	@Override
	public void run(ApplicationArguments args) throws Exception {
		DiscardServer.startServer();
	}

}
