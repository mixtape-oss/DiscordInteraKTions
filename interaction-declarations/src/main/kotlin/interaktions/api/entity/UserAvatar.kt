package interaktions.api.entity

import java.util.*

// Inspired by Kord
// https://github.com/kordlib/kord/blob/ce7f0a12e6b9267e2d13f7995a29c903e6d0edd8/core/src/main/kotlin/entity/User.kt#L85
public class UserAvatar(public val userId: ULong, public val discriminator: Int, public val avatarId: String?) {
    /**
     * The default avatar url for this user. Discord uses this for users who don't have a custom avatar set.
     */
    public val defaultUrl: String get() = "https://cdn.discordapp.com/embed/avatars/${discriminator.toInt() % 5}.png"

    /**
     * Whether the user has set their avatar.
     */
    public val isCustom: Boolean get() = avatarId != null

    /**
     * Whether the user has an animated avatar.
     */
    public val isAnimated: Boolean get() = avatarId?.startsWith("a_") ?: false

    /**
     * The supported format for this avatar
     */
    public val format: ImageFormat
        get() = when {
            isAnimated -> ImageFormat.GIF
            else -> ImageFormat.PNG
        }

    /**
     * The extension of the file for this avatar
     */
    public val formatExtension: String = format.extension

    /**
     * Gets the avatar url in a supported format (defined by [format]) and default size.
     */
    public val url: String get() = if (isCustom) "https://cdn.discordapp.com/avatars/$userId/$avatarId.$formatExtension" else defaultUrl

    public enum class ImageFormat {
        PNG,
        GIF;

        public val extension: String = name.lowercase(Locale.getDefault())
    }
}
