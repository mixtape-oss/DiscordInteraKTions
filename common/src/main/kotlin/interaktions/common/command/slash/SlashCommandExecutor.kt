package interaktions.common.command.slash

import interaktions.common.command.interaction.InteractionCommandExecutor
import interaktions.common.context.commands.ApplicationCommandContext
import interaktions.common.context.commands.slash.SlashCommandArguments

/**
 * This is the class that should be inherited if you
 * want to create an Slash Command.
 */
public abstract class SlashCommandExecutor : InteractionCommandExecutor() {
    public val conditions: MutableList<SlashCommandCondition> = mutableListOf()

    public abstract suspend fun execute(context: ApplicationCommandContext, args: SlashCommandArguments)
}
