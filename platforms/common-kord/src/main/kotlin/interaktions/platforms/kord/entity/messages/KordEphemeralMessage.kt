package interaktions.platforms.kord.entity.messages

import dev.kord.common.entity.DiscordMessage
import dev.kord.common.entity.Snowflake
import interaktions.common.entity.message.EphemeralMessage

public open class KordEphemeralMessage(public val handle: DiscordMessage) : EphemeralMessage {
    override val id: Snowflake by handle::id
    override val content: String by handle::content
}
