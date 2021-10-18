package net.perfectdreams.discordinteraktions.common.interactions

import dev.kord.common.entity.Snowflake
import net.perfectdreams.discordinteraktions.api.entities.Member
import net.perfectdreams.discordinteraktions.api.entities.User
import net.perfectdreams.discordinteraktions.common.entities.messages.Message

// TODO: channels
// TODO: roles
public data class ResolvedObjects(
    public val users: Map<Snowflake, User>?,
    public val members: Map<Snowflake, Member>?,
    public val messages: Map<Snowflake, Message>?
)
