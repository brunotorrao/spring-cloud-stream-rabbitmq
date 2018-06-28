package br.com.torrao.springcloudstreamrabbitmq.listener

import br.com.torrao.springcloudstreamrabbitmq.Sink.Companion.PLAYER_CREATED
import org.springframework.cloud.stream.annotation.Input
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
class PlayerCreatedListener{

    @StreamListener
    fun incoming(@Input(PLAYER_CREATED) messages: Flux<String>) {
        messages
                .map { it.toUpperCase() }
                .subscribe { println("player received $it") }
    }

}