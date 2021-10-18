package net.perfectdreams.discordinteraktions.platforms.kord.entities

import dev.kord.common.entity.DiscordUser
import dev.kord.common.entity.Snowflake
import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.api.entities.UserAvatar

public class KordUser(public val handle: DiscordUser) : User {
    override val id: Snowflake by handle::id
    override val name: String by handle::username
    override val discriminator: String by handle::discriminator
    override val avatar: UserAvatar = UserAvatar(id.value, discriminator.toInt(), handle.avatar)

    override val bot: Boolean
        get() = handle.bot.discordBoolean
}
