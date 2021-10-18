package net.perfectdreams.discordinteraktions.platforms.kord.entities

import dev.kord.common.entity.DiscordGuild
import net.perfectdreams.discordinteraktions.api.entities.Guild

class KordGuild(val handle: DiscordGuild) : Guild {
    override val id by handle::id
}
