package interaktions.declaration.command

import interaktions.declaration.command.message.MessageCommandExecutorDeclaration

public class MessageCommandDeclaration(
    name: String,
    public val executor: MessageCommandExecutorDeclaration // User/Message commands always requires an executor, that's why it is not nullable!
) : interaktions.declaration.command.InteractionCommandDeclaration(name)
