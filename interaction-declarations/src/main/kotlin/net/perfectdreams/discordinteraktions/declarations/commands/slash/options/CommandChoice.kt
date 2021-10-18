package net.perfectdreams.discordinteraktions.declarations.commands.slash.options

public class CommandChoice<T>(
	// We need to store the command option type due to type erasure
    public val type: CommandOptionType,
    public val name: String,
    public val value: T
)
