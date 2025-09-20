package com.mipa.readerandroid.repository.util

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class FileUtils {
    companion object{
        fun createPart(context: Context, uri: Uri, name: String): MultipartBody.Part {
            val mimeType = context.contentResolver.getType(uri) ?: "image/*"
            val extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType) ?: "jpg"
            val fileName = "${name}.$extension"

            // 一次性读入内存，避免 InputStream 被关闭
            val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                ?: return MultipartBody.Part.createFormData(name, fileName, ByteArray(0).toRequestBody())

            val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
            return MultipartBody.Part.createFormData(name, fileName, requestBody)
        }


        private fun getFileExtension(context: Context, uri: Uri): String {
            val mimeType = context.contentResolver.getType(uri)
            return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType) ?: ""
        }
    }
}