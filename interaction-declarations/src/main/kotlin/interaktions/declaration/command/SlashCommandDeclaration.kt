package interaktions.declaration.command

import interaktions.declaration.command.slash.SlashCommandExecutorDeclaration

public open class SlashCommandDeclaration(
    name: String,
    public val description: String,
    public val executor: SlashCommandExecutorDeclaration? = null,
    public val subcommands: List<interaktions.declaration.command.SlashCommandDeclaration>,
    public val subcommandGroups: List<interaktions.declaration.command.SlashCommandGroupDeclaration>
) : interaktions.declaration.command.InteractionCommandDeclaration(name)
