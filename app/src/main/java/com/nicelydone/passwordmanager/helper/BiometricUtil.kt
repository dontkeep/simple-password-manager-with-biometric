package com.nicelydone.passwordmanager.helper

import android.content.Context
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.util.concurrent.Executor

object BiometricUtil {

   fun setupBiometricAuthentication(
      context: Context,
      fragment: Fragment,
      onSuccess: () -> Unit,
      onFailure: () -> Unit,
      onError: () -> Unit
   ) {
      val executor: Executor = ContextCompat.getMainExecutor(context)

      val biometricPrompt = BiometricPrompt(fragment, executor, object : BiometricPrompt.AuthenticationCallback() {
         override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            Toast.makeText(context, "Authentication error: $errString", Toast.LENGTH_SHORT).show()
            onError()
         }

         override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            onSuccess()
         }

         override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show()
            onFailure()
         }
      })

      val promptInfo = BiometricPrompt.PromptInfo.Builder()
         .setTitle("Biometric login for password decryption")
         .setSubtitle("Log in using your biometric credential or device PIN/password")
         .setNegativeButtonText("Cancel")
         .build()

      val biometricManager = BiometricManager.from(context)
      when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
         BiometricManager.BIOMETRIC_SUCCESS -> {
            // Biometric authentication is available
            biometricPrompt.authenticate(promptInfo)
         }
         BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
            // Device doesn't have biometric hardware
            Toast.makeText(context, "No biometric hardware available", Toast.LENGTH_SHORT).show()
            onError()
         }
         BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
            // Biometric hardware is currently unavailable
            Toast.makeText(context, "Biometric hardware is currently unavailable", Toast.LENGTH_SHORT).show()
            onError()
         }
         BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
            // The user hasn't associated any biometric credentials with their account
            Toast.makeText(context, "No biometric credentials enrolled", Toast.LENGTH_SHORT).show()
            onError()
         }
      }
   }
}