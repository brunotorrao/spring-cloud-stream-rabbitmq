package br.com.torrao.springcloudstreamrabbitmq

import org.springframework.cloud.stream.annotation.Input
import org.springframework.messaging.SubscribableChannel

interface Sink {
    companion object {
        const val PLAYER_CREATED = "player-created"
    }

    @Input(PLAYER_CREATED)
    fun playerCreated(): SubscribableChannel
}