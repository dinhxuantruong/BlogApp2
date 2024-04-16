package com.example.blogapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.LinkedList

abstract class MySwipeHelper(
    context: Context, private val recyclerView: RecyclerView,
    private var buttonWith: Int
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private var bottomList: MutableList<MyButton>? = null
    private lateinit var gestureDetector: GestureDetector
    private var swipePosition = -1
    private var swipeThreshold = 0.5f
    private val buttonBuffer: MutableMap<Int, MutableList<MyButton>>

    private lateinit var removeQueue: LinkedList<Int>
    abstract fun instantiateButton(
        viewHolder: RecyclerView.ViewHolder,
        buffer: MutableList<MyButton>
    )

    private val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onContextClick(e: MotionEvent): Boolean {
            for (button in bottomList!!) {
                if (button.onClick(e.x, e.y)) {
                    break
                }
            }
            return true
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private val onTouchListener = View.OnTouchListener { _, event ->
        if (swipePosition < 0) return@OnTouchListener false
        val point = Point(event.rawX.toInt(), event.rawY.toInt())
        val swiperViewHolder = recyclerView.findViewHolderForAdapterPosition(swipePosition) ?: return@OnTouchListener false
        val swipeItem = swiperViewHolder.itemView
        val rect = Rect().apply { swipeItem.getGlobalVisibleRect(this) }

        return@OnTouchListener when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                if (rect.top < point.y && rect.bottom > point.y) {
                    gestureDetector.onTouchEvent(event)
                } else {
                    removeQueue.add(swipePosition)
                    swipePosition = -1
                    recoverSwipeItem()
                }
                true
            }
            else -> false
        }
    }


    init {
        this.bottomList = ArrayList()
        this.gestureDetector = GestureDetector(context, gestureListener)
        this.recyclerView.setOnTouchListener(onTouchListener)
        this.buttonBuffer = HashMap()
        this.removeQueue = IntLinkerList()

         attachSwipe()
    }

    private fun attachSwipe(){
        val itemTouchHelper = ItemTouchHelper(this)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    class IntLinkerList : LinkedList<Int>() {
        override fun contains(element: Int): Boolean {
            return false
        }

        override fun lastIndexOf(element: Int): Int {
            return element
        }

        override fun remove(element: Int): Boolean {
            return false
        }

        override fun indexOf(element: Int): Int {
            return element
        }

        override fun add(element: Int): Boolean {
            return if (contains(element))
                false
            else super.add(element)
        }
    }


    @Synchronized
    private fun recoverSwipeItem() {
        while (!removeQueue.isEmpty()) {
            val pos = removeQueue.poll()!!.toInt()
            if (pos > 1) {
                recyclerView.adapter!!.notifyItemChanged(pos)
            }
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val pos = viewHolder.adapterPosition
        if (swipePosition != pos)
            removeQueue.add(swipePosition)
        swipePosition = pos
        if (buttonBuffer.containsKey(swipePosition))
            bottomList = buttonBuffer[swipePosition]
        else
            bottomList!!.clear()
        buttonBuffer.clear()
        swipeThreshold = 0.5f*bottomList!!.size.toFloat()*buttonWith.toFloat()
        recoverSwipeItem()
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return 0.1f
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return 10f
    }

    override fun getSwipeVelocityThreshold(defaultValue: Float): Float {
        return 5.0f*defaultValue
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val pos = viewHolder.adapterPosition
        if (pos < 0) {
            swipePosition = pos
            return
        }

        val itemView = viewHolder.itemView
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            if (dX < 0) {
                var buffer: MutableList<MyButton> = ArrayList()
                if (!buttonBuffer.containsKey(pos)) {
                    instantiateButton(viewHolder, buffer)
                    buttonBuffer[pos] = buffer
                } else {
                    buffer = buttonBuffer[pos]!!
                }

                // Calculate the translationX based on the number of buttons and their width
                val translationX = dX * buffer.size.toFloat() * buttonWith.toFloat() / itemView.width

                val maxTranslationX = -buttonWith.toFloat() * buffer.size
                val finalTranslationX = translationX.coerceIn(maxTranslationX, 0f)

                // Draw the buttons based on the finalTranslationX
                drawButton(c, itemView, buffer, pos, finalTranslationX)

                // Adjust itemView's position based on finalTranslationX
                itemView.translationX = finalTranslationX + 20f
            } else {
                bottomList?.clear()
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }


    private fun drawButton(c: Canvas, itemView: View, buffer: MutableList<MyButton>, pos: Int, translationX: Float){
        var right = itemView.right.toFloat()
        val dButtonWith = -translationX / buffer.size
        for (button in buffer){
            val left = right - dButtonWith
            button.onDraw(c, RectF(left,itemView.top.toFloat(),right,itemView.bottom.toFloat()),pos)
            right = left
        }
        //if(translationX > - 50 ){ buttomList?.clear() }
    }


}