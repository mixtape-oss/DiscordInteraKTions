package net.perfectdreams.discordinteraktions.declarations.commands

/**
 * Base class of every interaction declaration, because all interactions share a [name]
 */
public sealed class InteractionCommandDeclaration(
    public val name: String
)
