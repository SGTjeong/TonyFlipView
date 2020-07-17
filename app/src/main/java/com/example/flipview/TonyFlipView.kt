package com.example.flipview

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import java.lang.Exception

class TonyFlipView : FrameLayout {
    constructor(context: Context) : super(context){
        init(context, null)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        init(context, attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        init(context, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes){
        init(context, attrs)
    }

    enum class FlipState{
        FRONT, BACK
    }

    enum class FlipDirection{
        VERTICAL_LEFT, VERTICAL_RIGHT, HORIZONTAL_UP, HORIZONTAL_DOWN
    }

    private var frontView : View? = null
    private var backView : View? = null

    private var flipState : FlipState = FlipState.FRONT
    private var flipDirection : FlipDirection = FlipDirection.VERTICAL_LEFT
    private var flipOnTouch : Boolean = true
    private var flipDuration : Int = 500
    private lateinit var flipLeftIn : Animator
    private lateinit var flipLeftOut : Animator

    private fun init(context: Context, attrs: AttributeSet?) {
        flipLeftIn = AnimatorInflater.loadAnimator(context, R.animator.flip_left)
        flipLeftOut = AnimatorInflater.loadAnimator(context, R.animator.flip_left_out)

        flipLeftOut.addListener(object : AnimatorListener{
            override fun onAnimationRepeat(animation: Animator?) {
            }
            override fun onAnimationEnd(animation: Animator?) {
                if(flipState == FlipState.FRONT){
                    flipState = FlipState.BACK
                    frontView?.visibility = View.GONE
                }
                else{
                    flipState = FlipState.FRONT
                    backView?.visibility = View.GONE
                }
            }
            override fun onAnimationCancel(animation: Animator?) {
            }
            override fun onAnimationStart(animation: Animator?) {
            }
        })
        attrs?.let{
            val attrArray = context.obtainStyledAttributes(attrs, R.styleable.tony_flip_view, 0, 0)
            try{
                flipOnTouch = attrArray.getBoolean(R.styleable.tony_flip_view_flipOnTouch, true)
                flipDuration = attrArray.getInt(R.styleable.tony_flip_view_flipDuration, 500)
                //flipDirection
            } catch (exception : Exception){
                Log.e("TonyFlipView", exception.toString())
            } finally {
                attrArray.recycle()
            }
        }

    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if(childCount > 2 || childCount < 1){
            throw Exception("Child count")
        }

        Log.e("WONSIK", "$childCount")
        findChildViews()
    }

    override fun addView(child: View?) {
        if(childCount > 2 || childCount < 1){
            throw Exception("Child count")
        }

        super.addView(child)
    }

    override fun removeView(view: View?) {
        super.removeView(view)

        findChildViews()
    }

    fun findChildViews(){
        if(childCount == 1){
            frontView = getChildAt(1)
        }

        else{
            frontView = getChildAt(1)
            backView = getChildAt(0)
        }

        if(frontView is FrameLayout){
            Log.e("WONSIK", "frontView")
        }
        if(backView is FrameLayout){
            Log.e("WONSIK", "backView")
        }
        flipState = FlipState.FRONT
        backView?.visibility = View.GONE
    }

    fun flip(){
        when(flipDirection){
            FlipDirection.VERTICAL_LEFT -> {
                frontView?.visibility = View.VISIBLE
                backView?.visibility = View.VISIBLE

                if(flipState == FlipState.FRONT){
                    flipLeftOut.setTarget(frontView)
                    flipLeftOut.start()
                    backView?.let{
                        flipLeftIn.setTarget(it)
                        flipLeftIn.start()
                    }
                }
                else{
                    backView?.let{
                        flipLeftOut.setTarget(it)
                        flipLeftOut.start()
                    }
                    flipLeftIn.setTarget(frontView)
                    flipLeftIn.start()
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(flipState == FlipState.FRONT){
            frontView?.dispatchTouchEvent(event)
        }
        else{
            backView?.dispatchTouchEvent(event)
        }
        return true
    }
}