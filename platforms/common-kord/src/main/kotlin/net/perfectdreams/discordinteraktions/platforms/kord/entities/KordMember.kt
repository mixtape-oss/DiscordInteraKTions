package net.perfectdreams.discordinteraktions.platforms.kord.entities

import dev.kord.common.entity.DiscordGuildMember
import dev.kord.common.entity.Snowflake
import net.perfectdreams.discordinteraktions.api.entities.Member
import net.perfectdreams.discordinteraktions.api.entities.User

public class KordMember(
    public val handle: DiscordGuildMember,
    override val user: User // The user object is here too because sometimes the handle user value may be null!
) : Member {
    override val roles: List<Snowflake> by handle::roles
}
