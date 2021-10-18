package net.perfectdreams.discordinteraktions.platforms.kord.entities

import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.DiscordChannel
import dev.kord.common.entity.Snowflake
import net.perfectdreams.discordinteraktions.api.entities.Channel

public class KordChannel(public val handle: DiscordChannel) : Channel {
    override val id: Snowflake by handle::id

    override val type: ChannelType by handle::type
}
