package br.com.torrao.springcloudstreamrabbitmq.listener

import br.com.torrao.springcloudstreamrabbitmq.Sink.Companion.PLAYER_CREATED
import org.springframework.amqp.core.Message
import org.springframework.amqp.core.MessageProperties
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.cloud.stream.annotation.Input
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.retry.Retry
import java.nio.charset.Charset
import java.time.Duration

@Component
class PlayerCreatedListener(val rabbitTemplate: RabbitTemplate) {

    @StreamListener
    fun incoming(@Input(PLAYER_CREATED) messages: Flux<String>) {
        messages.flatMap { message ->
            Mono.just(message)
                    .map { handleMessage(it) }
                    .retryWhen( getRetryPolicy() )
                    .onErrorResume { sendToDLQ(message) }
        }.log().subscribe()
    }

    private fun handleMessage(it: String?) {
        print("$it mono --- ")
        throw IllegalStateException()
    }

    private fun getRetryPolicy() : Retry<Any> {
        return Retry
                .anyOf<Any>(Exception::class.java)
                .retryMax(5)
                .exponentialBackoff(Duration.ofSeconds(1), Duration.ofSeconds(6))
                .doOnRetry { println("retrying message for the ${it.iteration()} time, exception= ${it.exception()}") }
    }

    private fun sendToDLQ(message: String): Mono<Nothing> {
        return Mono.fromRunnable<Nothing> {
            println("mono handling error")
            rabbitTemplate.send(
                    "DLX",
                    "$PLAYER_CREATED.social",
                    Message(message.toByteArray(Charset.defaultCharset()), MessageProperties())
            )
        }
    }
}