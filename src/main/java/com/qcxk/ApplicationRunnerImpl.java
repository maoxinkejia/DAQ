package com.qcxk;

import com.qcxk.component.netty.DiscardServer;
import com.qcxk.service.MessageService;
import com.qcxk.util.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApplicationRunnerImpl  implements ApplicationRunner {
	@Override
	public void run(ApplicationArguments args) {
		MessageService messageService = SpringUtil.getBean(MessageService.class);
		DiscardServer.startServer(messageService);
	}

}
