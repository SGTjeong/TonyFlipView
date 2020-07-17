package com.example.flipview

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import org.w3c.dom.Attr
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
    private var flipScale : Float = 0.8f

    private lateinit var flipLeftIn : AnimatorSet
    private lateinit var flipLeftOut : AnimatorSet
    private lateinit var flipRightIn : AnimatorSet
    private lateinit var flipRightOut : AnimatorSet
    private val animatorList = mutableListOf<AnimatorSet>()

    private fun init(context: Context, attrs: AttributeSet?) {
        loadAnimations()
        loadAttributes(attrs)
        applyAttributes()
    }

    private fun applyAttributes() {
        for(animator in animatorList){
            animator.childAnimations[0]?.duration = flipDuration.toLong()
            animator.childAnimations[1]?.duration = flipDuration/2L
            animator.childAnimations[2]?.duration = flipDuration/2L
            animator.childAnimations[3]?.duration = flipDuration/2L
            animator.childAnimations[4]?.duration = flipDuration/2L
            animator.childAnimations[5]?.duration = flipDuration/2L
            animator.childAnimations[4]?.startDelay = flipDuration/2L
            animator.childAnimations[5]?.startDelay = flipDuration/2L
        }
    }

    private fun loadAttributes(attrs : AttributeSet?) {
        attrs?.let{
            val attrArray = context.obtainStyledAttributes(attrs, R.styleable.tony_flip_view, 0, 0)
            try{
                flipDuration = attrArray.getInt(R.styleable.tony_flip_view_flipDuration, 500)
                flipScale = attrArray.getFloat(R.styleable.tony_flip_view_flipScale, 0.8f)
                //flipDirection
            } catch (exception : Exception){
                Log.e("TonyFlipView", exception.toString())
            } finally {
                attrArray.recycle()
            }
        }

    }

    private fun loadAnimations() {
        flipLeftIn = AnimatorInflater.loadAnimator(context, R.animator.flip_left) as AnimatorSet
        animatorList.add(flipLeftIn)
        flipLeftOut = AnimatorInflater.loadAnimator(context, R.animator.flip_left_out) as AnimatorSet
        animatorList.add(flipLeftOut)
        flipRightIn = AnimatorInflater.loadAnimator(context, R.animator.flip_right) as AnimatorSet
        animatorList.add(flipRightIn)
        flipRightOut = AnimatorInflater.loadAnimator(context, R.animator.flip_right_out) as AnimatorSet
        animatorList.add(flipRightOut)

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

        flipRightOut.addListener(object : AnimatorListener{
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
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        if(childCount > 2 || childCount < 1){
            throw Exception("Child count")
        }

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
        flipState = FlipState.FRONT
        backView?.visibility = View.GONE
    }

    fun flipLeft(){
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

    fun flipRight(){
        frontView?.visibility = View.VISIBLE
        backView?.visibility = View.VISIBLE

        if(flipState == FlipState.FRONT){
            flipRightOut.setTarget(frontView)
            flipRightOut.start()
            backView?.let{
                flipRightIn.setTarget(it)
                flipRightIn.start()
            }
        }
        else{
            backView?.let{
                flipRightOut.setTarget(it)
                flipRightOut.start()
            }
            flipLeftIn.setTarget(frontView)
            flipLeftIn.start()
        }
    }
}