package com.rysanek.fetchimagefilter.data.remote.apis

import com.rysanek.fetchimagefilter.data.remote.dtos.PictureDTO
import com.rysanek.fetchimagefilter.data.remote.dtos.UploadUrlDTO
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*
import java.io.File

interface FetchImageFilterAPI {
    
    @GET("/image")
    suspend fun fetchRemotePicturesList(): Response<List<PictureDTO>>
    
    @GET("/upload")
    suspend fun fetchUploadUrl(): Response<UploadUrlDTO>
    
    @Multipart
    @POST("/{url}")
    suspend fun uploadImage(
        @Path(value = "url", encoded = true) url: String,
        @Part("appid") appId: String,
        @Part("original") originalUrl: String,
        @Part imageFile: MultipartBody.Part
    ): Response<ResponseBody>
}