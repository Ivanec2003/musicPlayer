package com.example.yourapp.utils

import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

object PermissionHandler {

    private const val REQUIRED_PERMISSION = android.Manifest.permission.WRITE_EXTERNAL_STORAGE

    // Запускає перевірку та запит дозволу
    fun checkAndRequestPermission(context: Context, launcher: ActivityResultLauncher<String>) {
        if (ContextCompat.checkSelfPermission(context, REQUIRED_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            launcher.launch(REQUIRED_PERMISSION)
        }
    }

    // Обробляє результат запиту дозволу
    fun handlePermissionResult(isGranted: Boolean, context: Context, launcher: ActivityResultLauncher<String>) {
        if (!isGranted) {
            handleDeniedPermission(context, launcher)
        }
    }

    // Обробляє відсутність дозволу
    private fun handleDeniedPermission(context: Context, launcher: ActivityResultLauncher<String>) {
        AlertDialog.Builder(context)
            .setTitle("Permission Required")
            .setMessage("This app requires WRITE_EXTERNAL_STORAGE permission to function properly. Please grant the permission.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                // Запросити дозвіл знову
                launcher.launch(REQUIRED_PERMISSION)
            }
            .show()
    }
}