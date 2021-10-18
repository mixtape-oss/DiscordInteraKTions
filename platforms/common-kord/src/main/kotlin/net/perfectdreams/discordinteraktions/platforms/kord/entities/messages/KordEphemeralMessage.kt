package net.perfectdreams.discordinteraktions.platforms.kord.entities.messages

import dev.kord.common.entity.DiscordMessage
import dev.kord.common.entity.Snowflake
import net.perfectdreams.discordinteraktions.common.builder.message.modify.EphemeralInteractionOrFollowupMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.builder.message.modify.EphemeralMessageModifyBuilder
import net.perfectdreams.discordinteraktions.common.entities.messages.EphemeralMessage

public open class KordEphemeralMessage(public val handle: DiscordMessage) : EphemeralMessage {
    override val id: Snowflake by handle::id
    override val content: String by handle::content
}
