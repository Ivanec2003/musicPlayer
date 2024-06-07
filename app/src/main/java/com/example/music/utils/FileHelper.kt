package com.example.music.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

class FileHelper(private val fragment: Fragment) {
    private lateinit var selectAudioLauncher: ActivityResultLauncher<Intent>
    private lateinit var deleteFileLauncher: ActivityResultLauncher<Intent>

    init {
        initializeActivityLaunchers()
    }

    private fun initializeActivityLaunchers() {
        // Ініціалізація лаунчера для вибору аудіофайла
        selectAudioLauncher = fragment.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    // Обробка вибраного аудіофайла
                    handleSelectedAudioFile(uri)
                }
            }
        }

        // Ініціалізація лаунчера для видалення файла
        deleteFileLauncher = fragment.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Обробка результату видалення файла
                // Можна показати повідомлення про успішне видалення або обробити помилку
            }
        }
    }

    fun selectAudio() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "audio/*"
        }
        selectAudioLauncher.launch(intent)
    }

    fun deleteFile(uri: Uri) {
        val intent = Intent(Intent.ACTION_DELETE).apply {
            data = uri
        }
        deleteFileLauncher.launch(intent)
    }

    private fun handleSelectedAudioFile(uri: Uri) {
        // Обробка вибраного аудіофайла
    }
}