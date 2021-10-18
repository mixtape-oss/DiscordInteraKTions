package net.perfectdreams.discordinteraktions.declarations.commands

import net.perfectdreams.discordinteraktions.declarations.commands.slash.SlashCommandExecutorDeclaration

public open class SlashCommandDeclaration(
    name: String,
    public val description: String,
    public val executor: SlashCommandExecutorDeclaration? = null,
    public val subcommands: List<SlashCommandDeclaration>,
    public val subcommandGroups: List<SlashCommandGroupDeclaration>
) : InteractionCommandDeclaration(name)
