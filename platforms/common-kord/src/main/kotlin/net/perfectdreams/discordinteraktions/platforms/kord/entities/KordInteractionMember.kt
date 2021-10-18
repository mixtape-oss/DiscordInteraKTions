package net.perfectdreams.discordinteraktions.platforms.kord.entities

import dev.kord.common.entity.DiscordInteractionGuildMember
import dev.kord.common.entity.Snowflake
import net.perfectdreams.discordinteraktions.api.entities.Member
import net.perfectdreams.discordinteraktions.api.entities.User

// This is the same thing as KordMember, however an Interaction guild member does not have a deaf or mute flag
public class KordInteractionMember(
    public val handle: DiscordInteractionGuildMember,
    override val user: User // The user object is here too because sometimes the handle user value may be null!
) : Member {
    override val roles: List<Snowflake> by handle::roles
}
