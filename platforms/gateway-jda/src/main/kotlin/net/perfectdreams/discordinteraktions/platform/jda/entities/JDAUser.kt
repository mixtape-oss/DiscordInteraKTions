package net.perfectdreams.discordinteraktions.platform.jda.entities

import interaktions.api.entity.Snowflake
import interaktions.api.entity.User
import interaktions.api.entity.UserAvatar

class JDAUser(private val user: net.dv8tion.jda.api.entities.User) : User {
    override val id: Snowflake
        get() = Snowflake(user.idLong)
    override val name by user::name
    override val discriminator by user::discriminator
    override val avatar: UserAvatar
        get() = UserAvatar(id.value, discriminator.toInt(), user.avatarId)
    override val bot: Boolean
        get() = user.isBot
}
