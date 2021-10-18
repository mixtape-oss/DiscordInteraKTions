package net.perfectdreams.discordinteraktions.declarations.commands

import net.perfectdreams.discordinteraktions.declarations.commands.message.MessageCommandExecutorDeclaration

public class MessageCommandDeclaration(
    name: String,
    public val executor: MessageCommandExecutorDeclaration // User/Message commands always requires an executor, that's why it is not nullable!
) : InteractionCommandDeclaration(name)
