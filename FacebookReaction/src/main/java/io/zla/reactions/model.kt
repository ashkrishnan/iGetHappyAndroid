package io.zla.reactions

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.ArrayRes
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt

/**
 * Selected reaction callback.
 * @param position selected item position, or -1.
 * @return if reaction selector should close.
 */
typealias ReactionSelectedListener = (position: Int) -> Boolean

/**
 * Reaction text provider.
 * @param position position of current selected item in [ReactionsConfig.reactions].
 * @return optional reaction text, null for no text.
 */
typealias ReactionTextProvider = (position: Int) -> CharSequence?

data class Reaction(
        val image: Drawable,
        val scaleType: ImageView.ScaleType = ImageView.ScaleType.FIT_CENTER
)

data class ReactionsConfig(
    val reactions: Collection<Reaction>,
    @Px val reactionSize: Int,
    @Px val horizontalMargin: Int,
    @Px val verticalMargin: Int,
    @ColorInt val popupColor: Int,
    val reactionTextProvider: ReactionTextProvider,
    val textBackground: Drawable,
    @ColorInt val textColor: Int,
    val textHorizontalPadding: Int,
    val textVerticalPadding: Int,
    val textSize: Float
)

private val NO_TEXT_PROVIDER: ReactionTextProvider = { _ -> null }

class ReactionsConfigBuilder(val context: Context) {

    // DSL friendly property based values, with default or empty values replaced during build

    var reactions: Collection<Reaction> = emptyList()

    // reactions = listOf(R.drawable.img1, R.drawable.img2, ...)
    var ReactionsConfigBuilder.reactionsIds: IntArray
        get() = throw NotImplementedError()
        set(value) { withReactions(value) }

    @Px
    var reactionSize: Int =
            context.resources.getDimensionPixelSize(R.dimen.reactions_item_size)

    @Px
    var horizontalMargin: Int =
            context.resources.getDimensionPixelSize(R.dimen.reactions_item_margin)

    @Px var verticalMargin: Int = horizontalMargin

    @ColorInt
    var popupColor: Int = Color.WHITE

    var reactionTextProvider: ReactionTextProvider = NO_TEXT_PROVIDER

    // reactionTexts = R.array.my_texts
    var ReactionsConfigBuilder.reactionTexts: Int
        get() = throw NotImplementedError()
        set(@ArrayRes value) { withReactionTexts(value) }

    var textBackground: Drawable? = null

    @ColorInt
    var textColor: Int = Color.WHITE

    var textHorizontalPadding: Int = 0

    var textVerticalPadding: Int = 0

    var textSize: Float = 0f

    // Builder pattern for Java

    fun withReactions(reactions: Collection<Reaction>) = this.also {
        this.reactions = reactions
    }

    @JvmOverloads
    fun withReactions(
            res: IntArray,
            scaleType: ImageView.ScaleType = ImageView.ScaleType.FIT_CENTER
    ) = withReactions(res.map { Reaction(ContextCompat.getDrawable(context, it)!!, scaleType) })

    fun withReactionTexts(reactionTextProvider: ReactionTextProvider) = this.also {
        this.reactionTextProvider = reactionTextProvider
    }

    fun withReactionTexts(@ArrayRes res: Int) = this.also {
        reactionTextProvider = context.resources.getStringArray(res)::get
    }

    fun withReactionSize(reactionSize: Int) = this.also {
        this.reactionSize = reactionSize
    }

    fun withHorizontalMargin(horizontalMargin: Int) = this.also {
        this.horizontalMargin = horizontalMargin
    }

    fun withVerticalMargin(verticalMargin: Int) = this.also {
        this.verticalMargin = verticalMargin
    }

    fun withPopupColor(@ColorInt popupColor: Int) = this.also {
        this.popupColor = popupColor
    }

    fun withTextBackground(textBackground: Drawable) = this.also {
        this.textBackground = textBackground
    }

    fun withTextColor(@ColorInt textColor: Int) = this.also {
        this.textColor = textColor
    }

    fun withTextHorizontalPadding(textHorizontalPadding: Int) = this.also {
        this.textHorizontalPadding = textHorizontalPadding
    }

    fun withTextVerticalPadding(textVerticalPadding: Int) = this.also {
        this.textVerticalPadding = textVerticalPadding
    }

    fun withTextSize(textSize: Float) = this.also {
        this.textSize = textSize
    }

    fun build(): ReactionsConfig =
            ReactionsConfig(
                    reactions = reactions.takeIf { it.isNotEmpty() }
                            ?: throw IllegalArgumentException("Empty reactions"),
                    popupColor = popupColor,
                    reactionSize = reactionSize,
                    horizontalMargin = horizontalMargin,
                    verticalMargin = verticalMargin,
                    reactionTextProvider = reactionTextProvider,
                    textBackground = textBackground
                            ?: ContextCompat.getDrawable(context, R.drawable.reactions_text_background)!!,
                    textColor = textColor,
                    textHorizontalPadding = textHorizontalPadding.takeIf { it != 0 }
                            ?: context.resources.getDimension(R.dimen.reactions_text_horizontal_padding).roundToInt(),
                    textVerticalPadding = textVerticalPadding.takeIf { it != 0 }
                            ?: context.resources.getDimension(R.dimen.reactions_text_vertical_padding).roundToInt(),
                    textSize = textSize.takeIf { it != 0f }
                            ?: context.resources.getDimension(R.dimen.reactions_text_size)
            )
}
