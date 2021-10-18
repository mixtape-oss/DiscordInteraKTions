package net.perfectdreams.discordinteraktions.declarations.commands

public class SlashCommandGroupDeclaration(
    name: String,
    public val description: String,
    public val subcommands: List<SlashCommandDeclaration>
) : InteractionCommandDeclaration(name)
