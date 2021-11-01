package interaktions.platforms.kord.entity

import dev.kord.common.entity.DiscordUser
import dev.kord.common.entity.Snowflake
import interaktions.api.entity.User
import interaktions.api.entity.UserAvatar

public class KordUser(public val handle: DiscordUser) : User {
    override val id: Snowflake by handle::id
    override val name: String by handle::username
    override val discriminator: String by handle::discriminator
    override val avatar: UserAvatar = UserAvatar(id.value, discriminator.toInt(), handle.avatar)

    override val bot: Boolean
        get() = handle.bot.discordBoolean
}
