package interaktions.declaration.command.slash.options

public class CommandOption<T>(
    // We need to store the command option type due to type erasure
    public val type: CommandOptionType,
    public val name: String,
    public val description: String,
    public val choices: List<CommandChoice<T>>
)
