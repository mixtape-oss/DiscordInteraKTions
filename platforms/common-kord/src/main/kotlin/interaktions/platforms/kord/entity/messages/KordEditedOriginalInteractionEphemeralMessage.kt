package interaktions.platforms.kord.entity.messages

import dev.kord.common.entity.DiscordMessage
import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.message.modify.InteractionResponseModifyBuilder
import dev.kord.rest.service.RestClient
import interaktions.common.builder.message.modify.EphemeralInteractionOrFollowupMessageModifyBuilder
import interaktions.common.builder.message.modify.EphemeralMessageModifyBuilder
import interaktions.common.entity.message.EditableEphemeralMessage
import interaktions.platforms.kord.util.runIfNotMissing

public class KordEditedOriginalInteractionEphemeralMessage(
    private val rest: RestClient,
    private val applicationId: Snowflake,
    private val interactionToken: String,
    message: DiscordMessage
) : KordEphemeralMessage(message), EditableEphemeralMessage {
    override suspend fun editMessage(block: EphemeralMessageModifyBuilder.() -> Unit): EditableEphemeralMessage = editMessage(EphemeralInteractionOrFollowupMessageModifyBuilder().apply(block))

    override suspend fun editMessage(message: EphemeralMessageModifyBuilder): EditableEphemeralMessage {
        val newMessage = rest.interaction.modifyInteractionResponse(
            applicationId,
            interactionToken,
            InteractionResponseModifyBuilder().apply {
                runIfNotMissing(message.state.content) { this.content = it }
                runIfNotMissing(message.state.allowedMentions) { this.allowedMentions = it }
                runIfNotMissing(message.state.components) { this.components = it }
                runIfNotMissing(message.state.embeds) { this.embeds = it }
            }.toRequest()
        )

        return KordEditedOriginalInteractionEphemeralMessage(
            rest,
            applicationId,
            interactionToken,
            newMessage
        )
    }
}
