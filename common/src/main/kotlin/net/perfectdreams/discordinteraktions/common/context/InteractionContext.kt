package net.perfectdreams.discordinteraktions.common.context

import dev.kord.common.entity.Snowflake
import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.builder.message.create.EphemeralInteractionOrFollowupMessageCreateBuilder
import net.perfectdreams.discordinteraktions.common.builder.message.create.PublicInteractionOrFollowupMessageCreateBuilder
import net.perfectdreams.discordinteraktions.common.entities.messages.EditableEphemeralMessage
import net.perfectdreams.discordinteraktions.common.entities.messages.EditablePersistentMessage
import net.perfectdreams.discordinteraktions.common.entities.messages.EphemeralMessage
import net.perfectdreams.discordinteraktions.common.entities.messages.Message
import net.perfectdreams.discordinteraktions.common.entities.messages.PublicMessage
import net.perfectdreams.discordinteraktions.common.interactions.InteractionData

abstract class InteractionContext(
    var bridge: RequestBridge,
    val sender: User,
    val channelId: Snowflake,
    val data: InteractionData
) {
    val isDeferred
        get() = bridge.state.value != InteractionRequestState.NOT_REPLIED_YET
    var wasInitiallyDeferredEphemerally = false

    /**
     * Defers the application command request message with a public message
     */
    suspend fun deferChannelMessage() {
        if (isDeferred)
            error("Trying to defer something that was already deferred!")

        bridge.manager.deferChannelMessage()
        wasInitiallyDeferredEphemerally = false
    }

    /**
     * Defers the application command request message with a ephemeral message
     */
    suspend fun deferChannelMessageEphemerally() {
        if (isDeferred)
            error("Trying to defer something that was already deferred!")

        bridge.manager.deferChannelMessageEphemerally()
        wasInitiallyDeferredEphemerally = true
    }

    suspend fun sendMessage(block: PublicInteractionOrFollowupMessageCreateBuilder.() -> (Unit))
            = sendPublicMessage(PublicInteractionOrFollowupMessageCreateBuilder().apply(block))

    suspend fun sendEphemeralMessage(block: EphemeralInteractionOrFollowupMessageCreateBuilder.() -> (Unit))
            = sendEphemeralMessage(EphemeralInteractionOrFollowupMessageCreateBuilder().apply(block))

    private suspend fun sendPublicMessage(message: PublicInteractionOrFollowupMessageCreateBuilder): EditablePersistentMessage {
        // Check if state matches what we expect
        if (bridge.state.value == InteractionRequestState.DEFERRED_CHANNEL_MESSAGE)
            if (wasInitiallyDeferredEphemerally)
                error("Trying to send a public message but the message was originally deferred ephemerally! Change the \"deferMessage(...)\" call to be public")

        if (message.files.isNotEmpty() && !isDeferred) {
            // If the message has files and our current bridge state is "NOT_REPLIED_YET", then it means that we need to defer before sending the file!
            // (Because currently you can only send files by editing the original interaction message or with a follow up message
            deferChannelMessage()
        }

        return bridge.manager.sendPublicMessage(message)
    }

    private suspend fun sendEphemeralMessage(message: EphemeralInteractionOrFollowupMessageCreateBuilder): EditableEphemeralMessage {
        // Check if state matches what we expect
        if (bridge.state.value == InteractionRequestState.DEFERRED_CHANNEL_MESSAGE)
            if (!wasInitiallyDeferredEphemerally)
                error("Trying to send a ephemeral message but the message was originally deferred as public! Change the \"deferMessage(...)\" call to be ephemeral")

        return bridge.manager.sendEphemeralMessage(message)
    }
}