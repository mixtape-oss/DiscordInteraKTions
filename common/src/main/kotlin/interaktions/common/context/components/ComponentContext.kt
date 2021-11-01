package interaktions.common.context.components

import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Snowflake
import interaktions.api.entity.User
import interaktions.common.builder.message.modify.EphemeralInteractionMessageModifyBuilder
import interaktions.common.builder.message.modify.PublicInteractionMessageModifyBuilder
import interaktions.common.context.InteractionContext
import interaktions.common.context.RequestBridge
import interaktions.common.entity.message.EditableEphemeralMessage
import interaktions.common.entity.message.EditablePersistentMessage
import interaktions.common.entity.message.Message
import interaktions.common.interaction.InteractionData
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

public open class ComponentContext(
    bridge: RequestBridge,
    sender: User,
    channelId: Snowflake,
    public val message: Message,
    data: InteractionData,
    handle: DiscordInteraction
) : InteractionContext(bridge, sender, channelId, data, handle) {
    public suspend fun deferUpdateMessage() {
        if (!isDeferred) {
            bridge.manager.deferUpdateMessage()
        }
    }

    public suspend fun updateMessage(message: PublicInteractionMessageModifyBuilder): EditablePersistentMessage {
        // Check if state matches what we expect
        if (message.files?.isNotEmpty() == true && !isDeferred) {
            // If the message has files and our current bridge state is "NOT_REPLIED_YET", then it means that we need to defer before sending the file!
            // (Because currently you can only send files by editing the original interaction message or with a follow up message
            deferUpdateMessage()
        }

        return bridge.manager.updateMessage(message)
    }

    public suspend fun updateEphemeralMessage(message: EphemeralInteractionMessageModifyBuilder): EditableEphemeralMessage {
        return bridge.manager.updateEphemeralMessage(message)
    }
}

@OptIn(ExperimentalContracts::class)
public suspend inline fun ComponentContext.editMessage(build: PublicInteractionMessageModifyBuilder.() -> Unit): EditablePersistentMessage {
    contract {
        callsInPlace(build, InvocationKind.EXACTLY_ONCE)
    }

    val builder = PublicInteractionMessageModifyBuilder()
        .apply(build)

    return updateMessage(builder)
}

@OptIn(ExperimentalContracts::class)
public suspend inline fun ComponentContext.editEphemeralMessage(build: EphemeralInteractionMessageModifyBuilder.() -> Unit): EditableEphemeralMessage {
    contract {
        callsInPlace(build, InvocationKind.EXACTLY_ONCE)
    }

    val builder = EphemeralInteractionMessageModifyBuilder()
        .apply(build)

    return updateEphemeralMessage(builder)
}
