package net.perfectdreams.discordinteraktions.platforms.kord.entities

import dev.kord.common.entity.DiscordChannel
import net.perfectdreams.discordinteraktions.api.entities.Channel

class KordChannel(val handle: DiscordChannel) : Channel {
    override val id by handle::id

    override val type by handle::type
}
