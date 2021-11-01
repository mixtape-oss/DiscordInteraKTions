package net.perfectdreams.discordinteraktions.platform.jda.entities

import interaktions.api.entity.Guild
import interaktions.api.entity.Snowflake

class JDAGuild(private val guild: net.dv8tion.jda.api.entities.Guild) : Guild {
    override val id = Snowflake(guild.idLong)
}
