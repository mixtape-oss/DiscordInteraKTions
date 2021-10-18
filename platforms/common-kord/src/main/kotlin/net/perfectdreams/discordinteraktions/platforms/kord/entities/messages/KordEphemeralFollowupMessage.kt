package net.perfectdreams.discordinteraktions.platforms.kord.entities.messages

import dev.kord.common.entity.DiscordMessage
import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.message.modify.FollowupMessageModifyBuilder
import dev.kord.rest.service.RestClient
import net.perfectdreams.discordinteraktions.common.builder.message.modify.EphemeralInteractionOrFollowupMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.builder.message.modify.EphemeralMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.entities.messages.EditableEphemeralMessage
import net.perfectdreams.discordinteraktions.common.entities.messages.EphemeralMessage
import net.perfectdreams.discordinteraktions.platforms.kord.utils.runIfNotMissing

public open class KordEphemeralFollowupMessage(
    private val rest: RestClient,
    private val applicationId: Snowflake,
    private val interactionToken: String,
    public val handle: DiscordMessage
) : EphemeralMessage, EditableEphemeralMessage {
    override val id: Snowflake by handle::id

    override val content: String by handle::content

    override suspend fun editMessage(block: EphemeralMessageModifyBuilder.() -> Unit): EditableEphemeralMessage =
        editMessage(EphemeralInteractionOrFollowupMessageModifyBuilder().apply(block))

    override suspend fun editMessage(message: EphemeralMessageModifyBuilder): EditableEphemeralMessage {
        val newMessage = rest.interaction.modifyFollowupMessage(
            applicationId,
            interactionToken,
            handle.id,
            FollowupMessageModifyBuilder().apply {
                runIfNotMissing(message.state.content) { this.content = it }
                runIfNotMissing(message.state.allowedMentions) { this.allowedMentions = it }
                runIfNotMissing(message.state.components) { this.components = it }
                runIfNotMissing(message.state.embeds) { this.embeds = it }
            }.toRequest()
        )

        return KordEphemeralFollowupMessage(
            rest,
            applicationId,
            interactionToken,
            newMessage
        )
    }
}
