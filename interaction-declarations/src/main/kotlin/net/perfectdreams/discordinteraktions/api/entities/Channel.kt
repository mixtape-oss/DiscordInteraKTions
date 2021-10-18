package net.perfectdreams.discordinteraktions.api.entities

import dev.kord.common.entity.ChannelType
import dev.kord.common.entity.Snowflake

public interface Channel {
    public val id: Snowflake

    public val type: ChannelType
}
