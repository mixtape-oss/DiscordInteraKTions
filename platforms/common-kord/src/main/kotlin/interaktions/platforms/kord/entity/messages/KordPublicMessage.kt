package interaktions.platforms.kord.entity.messages

import dev.kord.common.entity.DiscordMessage
import dev.kord.common.entity.Snowflake
import interaktions.common.entity.message.PublicMessage

public open class KordPublicMessage(public val handle: DiscordMessage) : PublicMessage {
    override val id: Snowflake by handle::id

    override val content: String by handle::content
}
