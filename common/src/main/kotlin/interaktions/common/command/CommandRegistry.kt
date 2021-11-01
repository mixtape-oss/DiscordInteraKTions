package interaktions.common.command

import dev.kord.common.entity.Snowflake

public interface CommandRegistry {
    public suspend fun updateAllCommandsInGuild(guildId: Snowflake, deleteUnknownCommands: Boolean)

    public suspend fun updateAllGlobalCommands(deleteUnknownCommands: Boolean)
}
