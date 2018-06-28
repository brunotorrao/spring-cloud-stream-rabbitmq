package br.com.torrao.springcloudstreamrabbitmq

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.stream.annotation.EnableBinding

@SpringBootApplication
@EnableBinding(Sink::class)
class SpringCloudStreamRabbitmqApplication

fun main(args: Array<String>) {
    runApplication<SpringCloudStreamRabbitmqApplication>(*args)
}
