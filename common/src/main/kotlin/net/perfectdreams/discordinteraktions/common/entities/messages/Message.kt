package net.perfectdreams.discordinteraktions.common.entities.messages

import dev.kord.common.entity.Snowflake

public interface Message {
    public companion object {
        public val None: Message = object : Message {
            override val content: String
                get() = TODO("Not yet implemented")

            override val id: Snowflake
                get() = TODO("Not yet implemented")
        }
    }

    public val id: Snowflake
    public val content: String?
}
