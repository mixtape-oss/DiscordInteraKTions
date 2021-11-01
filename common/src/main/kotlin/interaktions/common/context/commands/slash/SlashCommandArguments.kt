package interaktions.common.context.commands.slash

import interaktions.declaration.command.slash.options.CommandOption
import interaktions.declaration.command.slash.options.CommandOptionType

public class SlashCommandArguments(public val types: Map<CommandOption<*>, Any?>) {
    public operator fun <T> get(argument: CommandOption<T>): T {
        if (!types.containsKey(argument) && argument.type is CommandOptionType.ToNullable)
            throw RuntimeException("Missing argument ${argument.name}!")

        return types[argument] as T
    }
}
