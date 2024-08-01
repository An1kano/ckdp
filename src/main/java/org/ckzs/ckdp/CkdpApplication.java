package org.ckzs.ckdp;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;


@Import(RocketMQAutoConfiguration.class)
@SpringBootApplication
@EnableScheduling
@Slf4j
public class CkdpApplication {

    public static void main(String[] args) {

        SpringApplication.run(CkdpApplication.class, args);
        log.info("服务启动...");
    }

}
