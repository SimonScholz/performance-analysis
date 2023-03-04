package io.github.simonscholz.infrastructure.adapter

import io.smallrye.mutiny.Multi
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.eclipse.microprofile.reactive.messaging.Message
import org.eclipse.microprofile.reactive.messaging.Outgoing
import javax.enterprise.context.ApplicationScoped

/**
 * Also see https://smallrye.io/smallrye-reactive-messaging/smallrye-reactive-messaging/3.4/jms/jms.html
 */
@ApplicationScoped
class JmsMessageHandler {
    @Incoming("ats")
    @Outgoing("atp")
    fun processMessageMulti(ats: Message<String>): Multi<Message<String>> =
        Multi.createFrom().items(ats)
            .onItem().transform { Message.of(it.payload.uppercase()) }
            .onItem().invoke { item -> println(item.payload) }
            .onCompletion().invoke { ats.ack() }
}
