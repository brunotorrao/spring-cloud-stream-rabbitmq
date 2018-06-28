package br.com.torrao.springcloudstreamrabbitmq.listener

import br.com.torrao.springcloudstreamrabbitmq.Sink.Companion.EVENT_CREATED
import org.springframework.cloud.stream.annotation.Input
import org.springframework.cloud.stream.annotation.StreamListener
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux

@Component
class EventCreatedListener{

    @StreamListener
    fun incoming(@Input(EVENT_CREATED) messages: Flux<String>) {
        messages
                .map { it.toUpperCase() }
                .subscribe { println("event received $it") }
    }
}