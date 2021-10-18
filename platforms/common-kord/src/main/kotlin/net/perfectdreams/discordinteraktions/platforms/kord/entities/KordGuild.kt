package net.perfectdreams.discordinteraktions.platforms.kord.entities

import dev.kord.common.entity.DiscordGuild
import dev.kord.common.entity.Snowflake
import net.perfectdreams.discordinteraktions.api.entities.Guild

public class KordGuild(public val handle: DiscordGuild) : Guild {
    override val id: Snowflake by handle::id
}
