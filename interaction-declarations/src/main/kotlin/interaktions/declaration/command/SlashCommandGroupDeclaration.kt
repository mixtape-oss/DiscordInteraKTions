package interaktions.declaration.command

public class SlashCommandGroupDeclaration(
    name: String,
    public val description: String,
    public val subcommands: List<interaktions.declaration.command.SlashCommandDeclaration>
) : interaktions.declaration.command.InteractionCommandDeclaration(name)
