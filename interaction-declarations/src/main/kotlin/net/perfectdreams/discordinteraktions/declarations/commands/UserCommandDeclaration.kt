package net.perfectdreams.discordinteraktions.declarations.commands

import net.perfectdreams.discordinteraktions.declarations.commands.user.UserCommandExecutorDeclaration

public class UserCommandDeclaration(
    name: String,
    public val executor: UserCommandExecutorDeclaration // User/Message commands always requires an executor, that's why it is not nullable!
) : InteractionCommandDeclaration(name)
