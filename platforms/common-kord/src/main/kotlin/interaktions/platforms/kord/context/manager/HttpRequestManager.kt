package interaktions.platforms.kord.context.manager

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.DiscordInteraction
import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.message.create.FollowupMessageCreateBuilder
import dev.kord.rest.service.RestClient
import interaktions.common.builder.message.create.EphemeralInteractionOrFollowupMessageCreateBuilder
import interaktions.common.builder.message.create.PublicInteractionOrFollowupMessageCreateBuilder
import interaktions.common.builder.message.modify.EphemeralInteractionMessageModifyBuilder
import interaktions.common.builder.message.modify.PublicInteractionMessageModifyBuilder
import interaktions.common.context.InteractionRequestState
import interaktions.common.context.RequestBridge
import interaktions.common.context.manager.RequestManager
import interaktions.common.entity.message.EditableEphemeralMessage
import interaktions.common.entity.message.EditablePersistentMessage
import interaktions.platforms.kord.entity.messages.KordEphemeralFollowupMessage
import interaktions.platforms.kord.entity.messages.KordOriginalInteractionEphemeralMessage
import interaktions.platforms.kord.entity.messages.KordOriginalInteractionPublicMessage
import interaktions.platforms.kord.entity.messages.KordPublicFollowupMessage

/**
 * On this request manager we'll handle the requests
 * by directly interacting with the Discord Rest API.
 *
 * @param rest The application rest client
 * @param applicationId The bot's application id
 * @param interactionToken The request's token
 * @param request The interaction (wrapped by the [InteractionRequestHandler]
 */
@OptIn(KordPreview::class)
public class HttpRequestManager(
    bridge: RequestBridge,
    public val rest: RestClient,
    public val applicationId: Snowflake,
    public val interactionToken: String,
    public val request: DiscordInteraction
) : RequestManager(bridge) {
    public companion object;

    init {
        require(bridge.state.value != InteractionRequestState.NOT_REPLIED_YET) { "HttpRequestManager shouldn't be in the NOT_REPLIED_YET state!" }
    }

    override suspend fun deferChannelMessage(): Nothing =
        error("Can't defer an interaction that was already deferred!")

    override suspend fun deferChannelMessageEphemerally(): Nothing =
        error("Can't defer an interaction that was already deferred!")

    override suspend fun createPublicMessage(message: PublicInteractionOrFollowupMessageCreateBuilder): EditablePersistentMessage {
        // *Technically* we can respond to the initial interaction via HTTP too
        val kordMessage = rest.interaction.createFollowupMessage(
            applicationId,
            request.token,
            FollowupMessageCreateBuilder(false).apply {
                this.content = message.content
                this.tts = message.tts
                this.allowedMentions = message.allowedMentions
                message.components?.let { this.components.addAll(it) }
                message.embeds?.let { this.embeds.addAll(it) }
                this.files.addAll(message.files)
            }.toRequest()
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED

        return KordPublicFollowupMessage(
            rest,
            applicationId,
            interactionToken,
            kordMessage
        )
    }

    override suspend fun createEphemeralMessage(message: EphemeralInteractionOrFollowupMessageCreateBuilder): EditableEphemeralMessage {
        // *Technically* we can respond to the initial interaction via HTTP too
        val kordMessage = rest.interaction.createFollowupMessage(
            applicationId,
            request.token,
            FollowupMessageCreateBuilder(true).apply {
                this.content = message.content
                this.tts = message.tts
                this.allowedMentions = message.allowedMentions
                message.components?.let { this.components.addAll(it) }
                message.embeds?.let { this.embeds.addAll(it) }
            }.toRequest()
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED

        return KordEphemeralFollowupMessage(
            rest,
            applicationId,
            interactionToken,
            kordMessage
        )
    }

    override suspend fun deferUpdateMessage(): Nothing =
        error("Can't defer an interaction that was already deferred!")

    override suspend fun updateMessage(message: PublicInteractionMessageModifyBuilder): EditablePersistentMessage {
        val interactionMessage = KordOriginalInteractionPublicMessage(
            rest,
            applicationId,
            interactionToken,
            null
        )

        val newMessage = interactionMessage.editMessage(message)

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED
        bridge.manager = HttpRequestManager(
            bridge,
            rest,
            applicationId,
            interactionToken,
            request
        )

        return newMessage
    }

    override suspend fun updateEphemeralMessage(message: EphemeralInteractionMessageModifyBuilder): EditableEphemeralMessage {
        val interactionMessage = KordOriginalInteractionEphemeralMessage(
            rest,
            applicationId,
            interactionToken,
            null
        )

        val newMessage = interactionMessage.editMessage(message)

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED
        bridge.manager = HttpRequestManager(
            bridge,
            rest,
            applicationId,
            interactionToken,
            request
        )

        return newMessage
    }
}
