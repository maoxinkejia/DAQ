package com.qcxk;

import com.qcxk.component.netty.DiscardServer;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunnerImpl  implements ApplicationRunner {
	@Override
	public void run(ApplicationArguments args) {
		DiscardServer.startServer();
	}

}
