package interaktions.common.command.message

import interaktions.common.command.interaction.InteractionCommandExecutor
import interaktions.common.context.commands.ApplicationCommandContext
import interaktions.common.entity.message.Message

/**
 * This is the class that should be inherited if you
 * want to create an Message Command.
 */
public abstract class MessageCommandExecutor : InteractionCommandExecutor() {
    public val conditions: MutableList<MessageCommandCondition> = mutableListOf()

    public abstract suspend fun execute(context: ApplicationCommandContext, targetMessage: Message)
}
