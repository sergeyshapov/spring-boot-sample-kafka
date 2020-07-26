/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sample.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SpringBootApplication
public class SampleKafkaApplication {
    private static Logger logger = LoggerFactory.getLogger(SampleKafkaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SampleKafkaApplication.class, args);
    }


    @Bean
    public Consumer<String> gsm1(){
        return payload -> {
            System.out.println("GSM 1111111111111");
        };
    }

    @Bean
    public Consumer<String> gsm2(){
        return payload -> {
            System.out.println("GSM 222222222");
        };
    }


    @Bean
    public Function<String, String> transform() {
        return payload -> payload.toUpperCase();
    }

    static class TestSource {
        private AtomicBoolean semaphore = new AtomicBoolean(true);
        @Bean
        public Supplier<Message<String>> sendTestData() {
            return () ->
                    MessageBuilder.
                            withPayload("bar").
                            setHeader("gsm",this.semaphore.getAndSet(!this.semaphore.get()) ? "gsm1":"gsm2").
                            build();
        }
    }

    static class TestSink {
        @Bean
        public Consumer<Message<String>> receive() {
            return payload -> {
                payload.getHeaders().forEach((k, v) -> System.out.println("k=" + k + ", v=" + v));
                logger.info("Data received: " + payload.getPayload());
            };
        }
    }
}
