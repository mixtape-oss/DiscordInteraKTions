package interaktions.common.context

import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Snowflake
import interaktions.api.entity.User
import interaktions.common.builder.message.create.EphemeralInteractionOrFollowupMessageCreateBuilder
import interaktions.common.builder.message.create.PublicInteractionOrFollowupMessageCreateBuilder
import interaktions.common.entity.message.EditableEphemeralMessage
import interaktions.common.entity.message.EditablePersistentMessage
import interaktions.common.interaction.InteractionData
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

public abstract class InteractionContext(
    public var bridge: RequestBridge,
    public val sender: User,
    public val channelId: Snowflake,
    public val data: InteractionData,
    public val handle: DiscordInteraction
) {
    public val isDeferred: Boolean
        get() = bridge.state.value != InteractionRequestState.NOT_REPLIED_YET

    public var deferredEphemerally: Boolean = false

    /**
     * Defers the application command request message with a public message
     */
    public suspend fun deferChannelMessage() {
        if (isDeferred)
            error("Trying to defer something that was already deferred!")

        bridge.manager.deferChannelMessage()
        deferredEphemerally = false
    }

    /**
     * Defers the application command request message with an ephemeral message
     */
    public suspend fun deferChannelMessageEphemerally() {
        if (isDeferred)
            error("Trying to defer something that was already deferred!")

        bridge.manager.deferChannelMessageEphemerally()
        deferredEphemerally = true
    }

    public suspend fun createPublicMessage(message: PublicInteractionOrFollowupMessageCreateBuilder): EditablePersistentMessage {
        // Check if state matches what we expect
        if (bridge.state.value == InteractionRequestState.DEFERRED_CHANNEL_MESSAGE) {
            if (deferredEphemerally) {
                error("Trying to send a public message but the message was originally deferred ephemerally! Change the \"deferMessage(...)\" call to be public")
            }
        }

        if (message.files.isNotEmpty() && !isDeferred) {
            // If the message has files and our current bridge state is "NOT_REPLIED_YET", then it means that we need to defer before sending the file!
            // (Because currently you can only send files by editing the original interaction message or with a follow up message
            deferChannelMessage()
        }

        return bridge.manager.createPublicMessage(message)
    }

    public suspend fun createEphemeralMessage(message: EphemeralInteractionOrFollowupMessageCreateBuilder): EditableEphemeralMessage {
        // Check if state matches what we expect
        if (bridge.state.value == InteractionRequestState.DEFERRED_CHANNEL_MESSAGE) {
            if (!deferredEphemerally) {
                error("Trying to send an ephemeral message but the message was originally deferred as public! Change the \"deferMessage(...)\" call to be ephemeral")
            }
        }

        return bridge.manager.createEphemeralMessage(message)
    }
}

@OptIn(ExperimentalContracts::class)
public suspend inline fun InteractionContext.sendMessage(build: PublicInteractionOrFollowupMessageCreateBuilder.() -> Unit): EditablePersistentMessage {
    contract {
        callsInPlace(build, InvocationKind.EXACTLY_ONCE)
    }

    val builder = PublicInteractionOrFollowupMessageCreateBuilder()
        .apply(build)

    return createPublicMessage(builder)
}

@OptIn(ExperimentalContracts::class)
public suspend inline fun InteractionContext.sendEphemeralMessage(build: EphemeralInteractionOrFollowupMessageCreateBuilder.() -> Unit): EditableEphemeralMessage {
    contract {
        callsInPlace(build, InvocationKind.EXACTLY_ONCE)
    }

    val builder = EphemeralInteractionOrFollowupMessageCreateBuilder()
        .apply(build)

    return createEphemeralMessage(builder)
}

