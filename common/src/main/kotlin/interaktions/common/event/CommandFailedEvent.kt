package interaktions.common.event

import interaktions.common.command.interaction.InteractionCommandExecutor
import interaktions.common.command.message.MessageCommandExecutor
import interaktions.common.command.slash.SlashCommandExecutor
import interaktions.common.command.user.UserCommandExecutor
import interaktions.common.context.commands.ApplicationCommandContext
import interaktions.declaration.command.InteractionCommandDeclaration
import interaktions.declaration.command.MessageCommandDeclaration
import interaktions.declaration.command.SlashCommandDeclaration
import interaktions.declaration.command.UserCommandDeclaration

public interface CommandFailedEvent : Event {
    public val context: ApplicationCommandContext

    public val exception: Throwable

    public val command: InteractionCommandDeclaration

    public val executor: InteractionCommandExecutor
}

public data class SlashCommandFailedEvent(
    override val command: SlashCommandDeclaration,
    override val executor: SlashCommandExecutor,
    override val context: ApplicationCommandContext,
    override val exception: Throwable
) : CommandFailedEvent

public data class MessageCommandFailedEvent(
    override val command: MessageCommandDeclaration,
    override val executor: MessageCommandExecutor,
    override val context: ApplicationCommandContext,
    override val exception: Throwable
) : CommandFailedEvent

public data class UserCommandFailedEvent(
    override val command: UserCommandDeclaration,
    override val executor: UserCommandExecutor,
    override val context: ApplicationCommandContext,
    override val exception: Throwable
) : CommandFailedEvent
