package com.pushup.counter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark

class PoseOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var pose: Pose? = null
    private val paint = Paint().apply {
        color = Color.GREEN
        strokeWidth = 8f
        style = Paint.Style.STROKE
    }
    
    private val pointPaint = Paint().apply {
        color = Color.RED
        strokeWidth = 12f
        style = Paint.Style.FILL
    }

    fun updatePose(newPose: Pose?) {
        pose = newPose
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        pose?.let { currentPose ->
            drawPoseLandmarks(canvas, currentPose)
            drawConnections(canvas, currentPose)
        }
    }

    private fun drawPoseLandmarks(canvas: Canvas, pose: Pose) {
        val landmarks = pose.allPoseLandmarks
        for (landmark in landmarks) {
            canvas.drawCircle(
                translateX(landmark.position.x),
                translateY(landmark.position.y),
                8f,
                pointPaint
            )
        }
    }

    private fun drawConnections(canvas: Canvas, pose: Pose) {
        val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
        val leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
        val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
        val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)
        val rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)
        val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)
        
        if (leftShoulder != null && leftElbow != null) {
            drawLine(canvas, leftShoulder, leftElbow)
        }
        if (leftElbow != null && leftWrist != null) {
            drawLine(canvas, leftElbow, leftWrist)
        }
        if (rightShoulder != null && rightElbow != null) {
            drawLine(canvas, rightShoulder, rightElbow)
        }
        if (rightElbow != null && rightWrist != null) {
            drawLine(canvas, rightElbow, rightWrist)
        }
    }

    private fun drawLine(canvas: Canvas, start: PoseLandmark, end: PoseLandmark) {
        canvas.drawLine(
            translateX(start.position.x),
            translateY(start.position.y),
            translateX(end.position.x),
            translateY(end.position.y),
            paint
        )
    }

    private fun translateX(x: Float): Float {
        return x * width
    }

    private fun translateY(y: Float): Float {
        return y * height
    }
}
