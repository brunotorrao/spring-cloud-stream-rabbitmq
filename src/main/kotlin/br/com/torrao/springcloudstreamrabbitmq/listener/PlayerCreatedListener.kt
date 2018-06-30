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
import java.nio.charset.Charset

@Component
class PlayerCreatedListener(val rabbitTemplate: RabbitTemplate) {

    @StreamListener
    fun incoming(@Input(PLAYER_CREATED) messages: Flux<String>) {
        messages.flatMap { message ->
            Mono.just(message)
                    .map { handleMessage(it) }
                    .retry(3)
                    .onErrorResume { sendToDLQ() }
        }.log().subscribe()
    }

    private fun handleMessage(it: String?) {
        print("$it mono --- ")
        throw IllegalStateException()
    }

    private fun sendToDLQ(): Mono<Nothing> {
        return Mono.fromRunnable<Nothing> {
            println("mono handling error")
            rabbitTemplate.send(
                    "DLX",
                    "$PLAYER_CREATED.social",
                    Message("lalala".toByteArray(Charset.defaultCharset()), MessageProperties())
            )
        }
    }
}