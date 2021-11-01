package interaktions.platforms.kord.entity

import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.DiscordChannel
import dev.kord.common.entity.Snowflake
import interaktions.api.entity.Channel

public class KordChannel(public val handle: DiscordChannel) : Channel {
    override val id: Snowflake by handle::id

    override val type: ChannelType by handle::type
}
