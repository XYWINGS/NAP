package com.example.nap.data.file_manager

import android.content.Context
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject
import kotlin.uuid.Uuid

class ImageFileManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    /*We read the file,copy it to internal storage with a random file name then return the url
    * also 'use' function will execute the code in try catch blocks with resource close function.
    * Finally wrapped insdie an io dispatcher for longer taks to stop stalling the app.*/
    private val imageDir: File = context.filesDir

    suspend fun copyImageToInternalStorage(url: String): String {

        val fileName = "IMG_${Uuid.random()}.jpg"
        val file = File(imageDir, fileName)

        withContext(Dispatchers.IO) {
            context.contentResolver.openInputStream(url.toUri())?.use { inputStream ->
                file.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        }

        return file.absolutePath
    }

    suspend fun deleteImage(url: String) {
        withContext(Dispatchers.IO) {
            val file = File(url)
            if (file.exists() && isInternal(file.absolutePath)) {
                file.delete()
            }
        }
    }


    fun isInternal(url: String): Boolean {
        return url.startsWith(imageDir.absolutePath)
    }

}
