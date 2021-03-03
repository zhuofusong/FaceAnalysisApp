/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.mlkit.vision.demo.kotlin.facedetector

import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.common.math.LongMath.pow
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.demo.GraphicOverlay
import com.google.mlkit.vision.demo.kotlin.VisionProcessorBase
import com.google.mlkit.vision.face.*
import java.util.Locale
import kotlin.math.abs
import kotlin.math.sqrt

/** Face Detector Demo.  */
class FaceDetectorProcessor(context: Context, detectorOptions: FaceDetectorOptions?) :
  VisionProcessorBase<List<Face>>(context) {

  private val detector: FaceDetector
  val findex: DoubleArray = doubleArrayOf(0.0)

  init {
    val options = detectorOptions
      ?: FaceDetectorOptions.Builder()
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .enableTracking()
        .build()

    detector = FaceDetection.getClient(options)

    Log.v(MANUAL_TESTING_LOG, "Face detector options: $options")
  }

  override fun stop() {
    super.stop()
    detector.close()
  }

  override fun detectInImage(image: InputImage): Task<List<Face>> {
    return detector.process(image)
  }

  override fun onSuccess(faces: List<Face>, graphicOverlay: GraphicOverlay) {
    for (face in faces) {
      graphicOverlay.add(FaceGraphic(graphicOverlay, face, findex))
      logExtrasForTesting(face)
      if (face.getContour(FaceContour.LEFT_EYE)!=null){
        logFaceStat(face)
      }
    }
  }

  override fun onFailure(e: Exception) {
    Log.e(TAG, "Face detection failed $e")
  }

  private fun logFaceStat(face: Face?){
    if (face != null) {
      val leftEyeContour = face.getContour(FaceContour.LEFT_EYE)?.points
      val rightEyeContour = face.getContour(FaceContour.RIGHT_EYE)?.points
      val lex_x: Float? = leftEyeContour?.get(0)?.x
      val lex_y: Float? = leftEyeContour?.get(0)?.y
      val len_x: Float? = leftEyeContour?.get(8)?.x
      val len_y: Float? = leftEyeContour?.get(8)?.y
      val rex_x: Float? = rightEyeContour?.get(8)?.x
      val rex_y: Float? = rightEyeContour?.get(8)?.y
      val ren_x: Float? = rightEyeContour?.get(0)?.x
      val ren_y: Float? = rightEyeContour?.get(0)?.y
      val length_en = sqrt((pow((len_x!! - ren_x!!).toLong(),2) + pow((len_y!! - ren_y!!).toLong(),2)).toDouble())
      val length_ex = sqrt((pow((lex_x!! - rex_x!!).toLong(),2) + pow((lex_y!! - rex_y!!).toLong(),2)).toDouble())
      if (length_en!=null){
        findex[0] = (length_en/length_ex)
        Log.i(MANUAL_TESTING_LOG, "en-en/ex-ex ratio is " + findex[0].toString())
      }
    }
  }

  companion object {
    private const val TAG = "FaceDetectorProcessor"
    private fun logExtrasForTesting(face: Face?) {
      if (face != null) {
        Log.v(
          MANUAL_TESTING_LOG,
          "face bounding box: " + face.boundingBox.flattenToString()
        )
        Log.v(
          MANUAL_TESTING_LOG,
          "face Euler Angle X: " + face.headEulerAngleX
        )
        Log.v(
          MANUAL_TESTING_LOG,
          "face Euler Angle Y: " + face.headEulerAngleY
        )
        Log.v(
          MANUAL_TESTING_LOG,
          "face Euler Angle Z: " + face.headEulerAngleZ
        )
        // All landmarks
        val landMarkTypes = intArrayOf(
          FaceLandmark.MOUTH_BOTTOM,
          FaceLandmark.MOUTH_RIGHT,
          FaceLandmark.MOUTH_LEFT,
          FaceLandmark.RIGHT_EYE,
          FaceLandmark.LEFT_EYE,
          FaceLandmark.RIGHT_EAR,
          FaceLandmark.LEFT_EAR,
          FaceLandmark.RIGHT_CHEEK,
          FaceLandmark.LEFT_CHEEK,
          FaceLandmark.NOSE_BASE
        )
        val landMarkTypesStrings = arrayOf(
          "MOUTH_BOTTOM",
          "MOUTH_RIGHT",
          "MOUTH_LEFT",
          "RIGHT_EYE",
          "LEFT_EYE",
          "RIGHT_EAR",
          "LEFT_EAR",
          "RIGHT_CHEEK",
          "LEFT_CHEEK",
          "NOSE_BASE"
        )
        landMarkTypes.indices.forEach { i ->
            val landmark = face.getLandmark(landMarkTypes[i])
          if (landmark == null) {
                Log.v(
                  MANUAL_TESTING_LOG,
                  "No landmark of type: " + landMarkTypesStrings[i] + " has been detected"
                )
          } else {
                val landmarkPosition = landmark.position
                val landmarkPositionStr =
                  String.format(Locale.US, "x: %f , y: %f", landmarkPosition.x, landmarkPosition.y)
                Log.v(
                  MANUAL_TESTING_LOG,
                  "Position for face landmark: " +
                          landMarkTypesStrings[i] +
                          " is :" +
                          landmarkPositionStr
                )
          }
        }
        Log.v(
          MANUAL_TESTING_LOG,
          "face left eye open probability: " + face.leftEyeOpenProbability
        )
        Log.v(
          MANUAL_TESTING_LOG,
          "face right eye open probability: " + face.rightEyeOpenProbability
        )
        Log.v(
          MANUAL_TESTING_LOG,
          "face smiling probability: " + face.smilingProbability
        )
        Log.v(
          MANUAL_TESTING_LOG,
          "face tracking id: " + face.trackingId
        )
      }
    }

  }
}
