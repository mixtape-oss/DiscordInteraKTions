package interaktions.declaration.command

import interaktions.declaration.command.user.UserCommandExecutorDeclaration

public class UserCommandDeclaration(
    name: String,
    public val executor: UserCommandExecutorDeclaration // User/Message commands always requires an executor, that's why it is not nullable!
) : interaktions.declaration.command.InteractionCommandDeclaration(name)
