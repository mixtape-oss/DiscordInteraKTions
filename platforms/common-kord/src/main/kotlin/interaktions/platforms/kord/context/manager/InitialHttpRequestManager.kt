package interaktions.platforms.kord.context.manager

import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.*
import dev.kord.common.entity.optional.Optional
import dev.kord.common.entity.optional.coerceToMissing
import dev.kord.common.entity.optional.optional
import dev.kord.rest.builder.message.create.InteractionResponseCreateBuilder
import dev.kord.rest.json.request.InteractionApplicationCommandCallbackData
import dev.kord.rest.json.request.InteractionResponseCreateRequest
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
import interaktions.platforms.kord.entity.messages.KordOriginalInteractionEphemeralMessage
import interaktions.platforms.kord.entity.messages.KordOriginalInteractionPublicMessage

/**
 * On this request manager we'll handle the requests
 * by directly interacting with the Discord Rest API.
 *
 * @param rest The application rest client
 * @param applicationId The bot's application ID
 * @param interactionToken The request's token
 * @param request The Discord Interaction request
 */
@OptIn(KordPreview::class)
public class InitialHttpRequestManager(
    bridge: RequestBridge,
    public val rest: RestClient,
    public val applicationId: Snowflake,
    public val interactionToken: String,
    public val request: DiscordInteraction
) : RequestManager(bridge) {
    public companion object;

    init {
        require(bridge.state.value == InteractionRequestState.NOT_REPLIED_YET) { "HttpRequestManager should be in the NOT_REPLIED_YET state!" }
    }

    override suspend fun deferChannelMessage() {
        rest.interaction.createInteractionResponse(
            request.id,
            interactionToken,
            InteractionResponseCreateRequest(
                InteractionResponseType.DeferredChannelMessageWithSource,
                InteractionApplicationCommandCallbackData().optional()
            )
        )

        bridge.state.value = InteractionRequestState.DEFERRED_CHANNEL_MESSAGE

        bridge.manager = HttpRequestManager(
            bridge,
            rest,
            applicationId,
            interactionToken,
            request
        )
    }

    override suspend fun deferChannelMessageEphemerally() {
        rest.interaction.createInteractionResponse(
            request.id,
            interactionToken,
            InteractionResponseCreateRequest(
                InteractionResponseType.DeferredChannelMessageWithSource,
                InteractionApplicationCommandCallbackData(flags = MessageFlags { + MessageFlag.Ephemeral }.optional())
                    .optional()
            )
        )

        bridge.state.value = InteractionRequestState.DEFERRED_CHANNEL_MESSAGE

        bridge.manager = HttpRequestManager(
            bridge,
            rest,
            applicationId,
            interactionToken,
            request
        )
    }

    override suspend fun createPublicMessage(message: PublicInteractionOrFollowupMessageCreateBuilder): EditablePersistentMessage {
        // *Technically* we can respond to the initial interaction via HTTP too
        rest.interaction.createInteractionResponse(
            request.id,
            request.token,
            InteractionResponseCreateBuilder(false).apply {
                this.content = message.content
                this.tts = message.tts
                this.allowedMentions = message.allowedMentions
                message.components?.let { this.components.addAll(it) }
                message.embeds?.let { this.embeds.addAll(it) }
                this.files.addAll(message.files)
            }.toRequest()
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED

        bridge.manager = HttpRequestManager(
            bridge,
            rest,
            applicationId,
            interactionToken,
            request
        )

        return KordOriginalInteractionPublicMessage(
            rest,
            applicationId,
            interactionToken,
            message.content
        )
    }

    override suspend fun createEphemeralMessage(message: EphemeralInteractionOrFollowupMessageCreateBuilder): EditableEphemeralMessage {
        // *Technically* we can respond to the initial interaction via HTTP too
        rest.interaction.createInteractionResponse(
            request.id,
            request.token,
            InteractionResponseCreateBuilder(true).apply {
                this.content = message.content
                this.tts = message.tts
                this.allowedMentions = message.allowedMentions
                message.components?.let { this.components.addAll(it) }
                message.embeds?.let { this.embeds.addAll(it) }
            }.toRequest()
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED

        bridge.manager = HttpRequestManager(
            bridge,
            rest,
            applicationId,
            interactionToken,
            request
        )

        return KordOriginalInteractionEphemeralMessage(
            rest,
            applicationId,
            interactionToken,
            message.content
        )
    }

    override suspend fun deferUpdateMessage() {
        rest.interaction.createInteractionResponse(
            request.id,
            interactionToken,
            InteractionResponseCreateRequest(
                InteractionResponseType.DeferredUpdateMessage,
                InteractionApplicationCommandCallbackData().optional()
            )
        )

        bridge.state.value = InteractionRequestState.DEFERRED_UPDATE_MESSAGE

        bridge.manager = HttpRequestManager(
            bridge,
            rest,
            applicationId,
            interactionToken,
            request
        )
    }

    override suspend fun updateMessage(message: PublicInteractionMessageModifyBuilder): EditablePersistentMessage {
        rest.interaction.createInteractionResponse(
            request.id,
            interactionToken,
            InteractionResponseCreateRequest(
                type = InteractionResponseType.UpdateMessage,
                data = Optional(
                    InteractionApplicationCommandCallbackData(
                        content = Optional(message.content).coerceToMissing(),
                        embeds = Optional(message.embeds?.map { it.toRequest() } ?: listOf()),
                        allowedMentions = Optional(message.allowedMentions?.build()).coerceToMissing(),
                        components = message.components?.map { it.build() }.optional().coerceToMissing(),
                        flags = MessageFlags {}.optional()
                    )
                )
            )
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED

        bridge.manager = HttpRequestManager(
            bridge,
            rest,
            applicationId,
            interactionToken,
            request
        )

        return KordOriginalInteractionPublicMessage(
            rest,
            applicationId,
            interactionToken,
            message.content
        )
    }

    override suspend fun updateEphemeralMessage(message: EphemeralInteractionMessageModifyBuilder): EditableEphemeralMessage {
        rest.interaction.createInteractionResponse(
            request.id,
            interactionToken,
            InteractionResponseCreateRequest(
                type = InteractionResponseType.UpdateMessage,
                data = Optional(
                    InteractionApplicationCommandCallbackData(
                        content = Optional(message.content).coerceToMissing(),
                        embeds = Optional(message.embeds?.map { it.toRequest() } ?: listOf()),
                        allowedMentions = Optional(message.allowedMentions?.build()).coerceToMissing(),
                        components = message.components?.map { it.build() }.optional().coerceToMissing(),
                        flags = MessageFlags {}.optional()
                    )
                )
            )
        )

        bridge.state.value = InteractionRequestState.ALREADY_REPLIED
        bridge.manager = HttpRequestManager(
            bridge,
            rest,
            applicationId,
            interactionToken,
            request
        )

        return KordOriginalInteractionEphemeralMessage(
            rest,
            applicationId,
            interactionToken,
            message.content
        )
    }
}
