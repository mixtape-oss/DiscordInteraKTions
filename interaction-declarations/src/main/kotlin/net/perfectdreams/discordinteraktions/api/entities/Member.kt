package net.perfectdreams.discordinteraktions.api.entities

import dev.kord.common.entity.Snowflake

public interface Member {
    public val user: User
    public val roles: List<Snowflake>
}
