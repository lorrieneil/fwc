package com.pushup.counter

import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark
import kotlin.math.atan2
import kotlin.math.sqrt

class PushUpDetector {
    
    private var isDown = false
    private var pushUpCount = 0
    private val downThreshold = 90f
    private val upThreshold = 160f
    
    fun detectPushUp(pose: Pose): PushUpResult {
        val leftShoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
        val leftElbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
        val leftWrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
        
        val rightShoulder = pose.getPoseLandmark(PoseLandmark.RIGHT_SHOULDER)
        val rightElbow = pose.getPoseLandmark(PoseLandmark.RIGHT_ELBOW)
        val rightWrist = pose.getPoseLandmark(PoseLandmark.RIGHT_WRIST)
        
        if (leftShoulder == null || leftElbow == null || leftWrist == null ||
            rightShoulder == null || rightElbow == null || rightWrist == null) {
            return PushUpResult(pushUpCount, PushUpState.UNKNOWN, 0f)
        }
        
        val leftAngle = calculateAngle(
            leftShoulder.position,
            leftElbow.position,
            leftWrist.position
        )
        
        val rightAngle = calculateAngle(
            rightShoulder.position,
            rightElbow.position,
            rightWrist.position
        )
        
        val avgAngle = (leftAngle + rightAngle) / 2
        
        val leftHip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
        val rightHip = pose.getPoseLandmark(PoseLandmark.RIGHT_HIP)
        
        val bodyAlignment = if (leftShoulder != null && leftHip != null && rightShoulder != null && rightHip != null) {
            checkBodyAlignment(leftShoulder.position, leftHip.position, rightShoulder.position, rightHip.position)
        } else {
            true
        }
        
        if (!bodyAlignment) {
            return PushUpResult(pushUpCount, PushUpState.POOR_FORM, avgAngle)
        }
        
        val state = when {
            avgAngle < downThreshold -> {
                if (!isDown) {
                    isDown = true
                    PushUpState.DOWN
                } else {
                    PushUpState.DOWN
                }
            }
            avgAngle > upThreshold -> {
                if (isDown) {
                    isDown = false
                    pushUpCount++
                    PushUpState.UP_COUNTED
                } else {
                    PushUpState.UP
                }
            }
            else -> PushUpState.TRANSITION
        }
        
        return PushUpResult(pushUpCount, state, avgAngle)
    }
    
    private fun calculateAngle(
        firstPoint: android.graphics.PointF,
        midPoint: android.graphics.PointF,
        lastPoint: android.graphics.PointF
    ): Float {
        val radians = atan2(lastPoint.y - midPoint.y, lastPoint.x - midPoint.x) -
                atan2(firstPoint.y - midPoint.y, firstPoint.x - midPoint.x)
        
        var angle = Math.abs(radians * 180.0 / Math.PI)
        
        if (angle > 180.0) {
            angle = 360.0 - angle
        }
        
        return angle.toFloat()
    }
    
    private fun checkBodyAlignment(
        leftShoulder: android.graphics.PointF,
        leftHip: android.graphics.PointF,
        rightShoulder: android.graphics.PointF,
        rightHip: android.graphics.PointF
    ): Boolean {
        val leftDist = distance(leftShoulder.x, leftShoulder.y, leftHip.x, leftHip.y)
        val rightDist = distance(rightShoulder.x, rightShoulder.y, rightHip.x, rightHip.y)
        
        val avgDist = (leftDist + rightDist) / 2
        val shoulderHipAngleThreshold = 30f
        
        val leftAngle = atan2(leftHip.y - leftShoulder.y, leftHip.x - leftShoulder.x) * 180 / Math.PI
        val rightAngle = atan2(rightHip.y - rightShoulder.y, rightHip.x - rightShoulder.x) * 180 / Math.PI
        
        return Math.abs(leftAngle - rightAngle) < shoulderHipAngleThreshold
    }
    
    private fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1))
    }
    
    fun reset() {
        isDown = false
        pushUpCount = 0
    }
    
    data class PushUpResult(
        val count: Int,
        val state: PushUpState,
        val angle: Float
    )
    
    enum class PushUpState {
        UP,
        DOWN,
        TRANSITION,
        UP_COUNTED,
        POOR_FORM,
        UNKNOWN
    }
}
