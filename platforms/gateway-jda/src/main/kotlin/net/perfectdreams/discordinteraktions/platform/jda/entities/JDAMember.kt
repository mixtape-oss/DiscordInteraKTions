package net.perfectdreams.discordinteraktions.platform.jda.entities

import interaktions.api.entity.Member
import interaktions.api.entity.Snowflake
import interaktions.api.entity.User
import interaktions.api.entity.UserAvatar

class JDAMember(private val member: net.dv8tion.jda.api.entities.Member) : Member {
    override val user: User
        get() = JDAUser(member.user)
    override val roles: List<Snowflake>
        get() = member.roles.map { Snowflake(it.idLong) }
}
