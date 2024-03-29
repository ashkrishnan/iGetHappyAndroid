package io.zla.reactions

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.PopupWindow

/**
 * Entry point for reaction popup.
 */
class ReactionPopup @JvmOverloads constructor(
    context: Context,
    reactionsConfig: ReactionsConfig,
    var reactionSelectedListener: ReactionSelectedListener? = null
) : PopupWindow(context), View.OnTouchListener, View.OnLongClickListener {


    private val rootView = FrameLayout(context).also {
        it.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
    private val view: ReactionViewGroup by lazy(LazyThreadSafetyMode.NONE) {
        // Lazily inflate content during first display
        ReactionViewGroup(context, reactionsConfig).also {
            it.layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )

            it.reactionSelectedListener = reactionSelectedListener

            rootView.addView(it)
        }.also { it.dismissListener = ::dismiss }
    }

    init {
        contentView = rootView
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.MATCH_PARENT
        isFocusable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (!isShowing) {
            // Show fullscreen with button as context provider
            showAtLocation(v, Gravity.CENTER_HORIZONTAL, 0, 0)
            view.show(event, v)
        }
        return view.onTouchEvent(event)
    }


    override fun onLongClick(p0: View?): Boolean {
        // mLongClickX = 60.801117
        // mLongClickY = 12.467468

        if (!isShowing) {
            // Show fullscreen with button as context provider
            showAtLocation(p0, Gravity.LEFT, 0, 0)
            view.showOnLong(p0!!)
        }
        return true
    }

    override fun dismiss() {
        view.dismiss()
        super.dismiss()
    }
}
