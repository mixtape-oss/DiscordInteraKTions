package interaktions.declaration.command.slash.options

public interface CommandOptionType {
    public val isNullable: Boolean
        get() = false

    public interface ToNullable {
        public fun toNullable(): Nullable
    }

    public abstract class Nullable : CommandOptionType {
        override val isNullable: Boolean = true
    }

    // ===[ TYPES ]===
    // String
    public object String : ToNullable, CommandOptionType {
        override fun toNullable(): NullableString = NullableString
    }

    public object NullableString : Nullable()

    // Integer
    public object Integer : ToNullable, CommandOptionType {
        override fun toNullable(): NullableInteger = NullableInteger
    }

    public object NullableInteger : Nullable()

    // Number
    public object Number : ToNullable, CommandOptionType {
        override fun toNullable(): NullableNumber = NullableNumber
    }

    public object NullableNumber : Nullable()

    // Boolean
    // Can't be named "Boolean" because that causes Kotlin to go crazy
    public object Bool : ToNullable, CommandOptionType {
        override fun toNullable(): NullableBool = NullableBool
    }

    public object NullableBool : Nullable()

    // User
    public object User : ToNullable, CommandOptionType {
        override fun toNullable(): NullableUser = NullableUser
    }

    public object NullableUser : Nullable()

    // Channel
    public object Channel : ToNullable, CommandOptionType {
        override fun toNullable(): NullableChannel = NullableChannel
    }

    public object NullableChannel : Nullable()

    // Role
    public object Role : ToNullable, CommandOptionType {
        override fun toNullable(): NullableRole = NullableRole
    }

    public object NullableRole : Nullable()

    // Mentionable
    public object Mentionable : ToNullable, CommandOptionType {
        override fun toNullable(): NullableMentionable = NullableMentionable
    }

    public object NullableMentionable : Nullable()
}
