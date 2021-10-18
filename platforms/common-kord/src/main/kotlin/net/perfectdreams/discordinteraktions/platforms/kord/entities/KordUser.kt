package net.perfectdreams.discordinteraktions.platforms.kord.entities

import dev.kord.common.entity.DiscordUser
import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.api.entities.UserAvatar

class KordUser(val handle: DiscordUser) : User {
    override val id by handle::id
    override val name by handle::username
    override val discriminator by handle::discriminator
    override val avatar = UserAvatar(id.value, discriminator.toInt(), handle.avatar)

    override val bot: Boolean
        get() = handle.bot.discordBoolean
}
