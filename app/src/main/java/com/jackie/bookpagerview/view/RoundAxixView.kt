//package com.jackie.bookpagerview.view
//
//import android.content.Context
//import android.graphics.Canvas
//import android.graphics.Path
//import android.graphics.PointF
//import android.util.Log
//import android.view.View
//import android.view.View.MeasureSpec.EXACTLY
//import java.util.*
//
///**
// * Created by Jackie on 2018/9/11.
// */
//class RoundAxixView(context: Context?) : View(context) {
//    private var mWidth: Float = 0.toFloat()
//    private var mHeight: Float = 0.toFloat()
//    private val TAG = "StrategyAxisView"
//    private val count = 13
//    private val mMaxValue = 20f
//    private val mMinValue = 0
//    private var mPerYLength: Float = 0.toFloat() //平均每一份Y的长度
//    private var mPerXLength: Int = 0 //平均每一份X
//    private val mDateArray = floatArrayOf(15f, 17f, 14f, 14.5f, 17f, 14.8f, 14.1f, 13f, 11.5f, 7f, 6f, 8f, 3f)
//    private var pathLine: Path? = null
//
//    internal var startX = 0f
//    internal var startY = 0f
//    internal var endX = 0f
//    internal var endY = 0f
//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//        val widthSpecMode = View.MeasureSpec.getMode(widthMeasureSpec)
//        val widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec)
//        val heightSpecMode = View.MeasureSpec.getMode(heightMeasureSpec)
//        val heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec)
//        if (widthSpecMode == EXACTLY && heightSpecMode == EXACTLY) {
//            setMeasuredDimension(widthSpecSize, heightSpecSize)
//            mWidth = measuredWidth.toFloat()
//            mHeight = measuredHeight.toFloat()
//            Log.i(TAG, "onMeasure: -----$mWidth     $mHeight")
//        }
//    }
//    var pointArrayList: ArrayList<PointF> = ArrayList()
//    private fun initPoint() {
//        if (pointArrayList != null && pointArrayList.size > 0) {
//            return
//        }
//        for (i in 0..mDateArray.size) {
//            startX = (i * mPerXLength).toFloat()
//            startY = (mMaxValue - mDateArray[i]) * mPerYLength
//            Log.i(TAG, "initPoint: ------$startX--$startY")
//            pointArrayList.add(PointF(startX, startY))
//            if (i + 1 == mDateArray.size - 1) {
//                endX = ((i + 1) * mPerXLength).toFloat()
//                endY = (mMaxValue - mDateArray[i + 1]) * mPerYLength
//                pointArrayList.add(PointF(endX, endY))
//                Log.i(TAG, "initPoint: ---2---$endX--$endY")
//                break
//            }
//        }
//        val mmP = ArrayList<PointF>()
//        for (i in 0 .. pointArrayList.size) {
//            mmP.add(PointF(pointArrayList.get(i).x, pointArrayList.get(i).y))
//        }
//        Log.i(TAG, "init: -------" + mmP.size)
//        calculateControlPoint(mmP)
//    }
//
//    override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)
//    }
//
//    private val SMOOTHNESS = 0.4f
//    var mControlPointList: Array<PointF> = arrayOf()
//    private fun calculateControlPoint(pointList: List<PointF>) {
//
//        if (pointList.size <= 1) {
//            return
//        }
//        for ((i, point) in pointList.withIndex()) {
//            when (i) {
//                0 -> {//第一项
//                    //添加后控制点
//                    val nextPoint = pointList[i + 1]
//                    val controlX = point.x + (nextPoint.x - point.x) * SMOOTHNESS
//                    val controlY = point.y
//                    mControlPointList.add(PointF(controlX, controlY))
//                }
//                pointList.size - 1 -> {//最后一项
//                    //添加前控制点
//                    val lastPoint = pointList[i - 1]
//                    val controlX = point.x - (point.x - lastPoint.x) * SMOOTHNESS
//                    val controlY = point.y
//                    mControlPointList.add(PointF(controlX, controlY))
//                }
//                else -> {//中间项
//                    val lastPoint = pointList[i - 1]
//                    val nextPoint = pointList[i + 1]
//                    val k = (nextPoint.y - lastPoint.y) / (nextPoint.x - lastPoint.x)
//                    val b = point.y - k * point.x
//                    //添加前控制点
//                    val lastControlX = point.x - (point.x - lastPoint.x) * SMOOTHNESS
//                    val lastControlY = k * lastControlX + b
//                    mControlPointList.add(PointF(lastControlX, lastControlY))
//                    //添加后控制点
//                    val nextControlX = point.x + (nextPoint.x - point.x) * SMOOTHNESS
//                    val nextControlY = k * nextControlX + b
//                    mControlPointList.add(PointF(nextControlX, nextControlY))
//                }
//            }
//        }
//    }
//
//
//}