package net.perfectdreams.discordinteraktions.common.context.commands.slash

import net.perfectdreams.discordinteraktions.declarations.commands.slash.options.CommandOption
import net.perfectdreams.discordinteraktions.declarations.commands.slash.options.CommandOptionType

public class SlashCommandArguments(public val types: Map<CommandOption<*>, Any?>) {
    public operator fun <T> get(argument: CommandOption<T>): T {
        if (!types.containsKey(argument) && argument.type is CommandOptionType.ToNullable)
            throw RuntimeException("Missing argument ${argument.name}!")

        return types[argument] as T
    }
}
