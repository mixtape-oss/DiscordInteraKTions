package net.perfectdreams.discordinteraktions.api.entities

import dev.kord.common.entity.Snowflake

interface Channel {
    val id: Snowflake
    val idLong: Long
        get() = id.value
}