package com.example.singhrahuldeep.igethappy.api


import com.example.singhrahuldeep.igethappy.model.input.ForgotPasswordInput
import com.example.singhrahuldeep.igethappy.model.input.LoginInputModel
import com.example.singhrahuldeep.igethappy.model.input.UpdatePostInputModel
import com.example.singhrahuldeep.igethappy.model.output.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*

/**
 * Created by Gharjyot Singh
 */


interface GitHubService {
    @POST("auth/login")
    fun loginUser(@Body input: LoginInputModel): Call<LoginResponseModel>

    @Multipart
    @POST("auth/login")
    fun loginForm(@PartMap mHashMap: HashMap<String, RequestBody>): Call<LoginResponseModel>

    @GET
    fun checkSocialIdExist(@Url url: String): Call<SocialIdExistResponse>

    @GET
    fun checkEmailExist(@Url url: String): Call<EmailExistModelResponse>


    @GET
    fun findLanguagesList(@Url url: String): Call<GetLanguageResponse>

    @POST("auth/forgotPassword")
    fun forgotPass(@Body input: ForgotPasswordInput): Call<ForgotPasswordResponse>

    @Multipart
    @POST("users")
    fun signUp(@PartMap mHashMap: HashMap<String, RequestBody>, @Part file: MultipartBody.Part): Call<SignUpResponse>

    @Multipart
    @POST("upload")
    fun facialRecognition(@Part file: MultipartBody.Part): Call<FacialResponse>


    @POST("chat")
    fun chatBot(): Call<ChatBotResponseModel>

    @GET("community/posts")
    fun fetchPost(@QueryMap params: Map<String, String>): Call<GetPostResponse>

    @Multipart
    @POST("community/posts")
    fun addPost(@PartMap mHashMap: HashMap<String, RequestBody>): Call<AddPostResponse>

    @Multipart
    @POST("community/posts")
    fun addPost(@PartMap mHashMap: HashMap<String, RequestBody>, @Part file: MultipartBody.Part): Call<AddPostResponse>

    @Multipart
    @POST("community/posts/likes")
    fun likePost(@PartMap mHashMap: HashMap<String, RequestBody>): Call<LikePostResponseModel>

    @Multipart
    @PATCH("community/posts/{id}")
    fun updatePost(@Path("id") id: String, @PartMap mHashMap: HashMap<String, RequestBody>, @Part file: MultipartBody.Part): Call<AddPostResponse>

    @PATCH("community/posts/{id}")
    fun updateTestPost(@Path("id") id: String, @Body model: UpdatePostInputModel): Call<AddPostResponse>

    @Multipart
    @POST("careReceivers")
    fun addCareReceiver(@PartMap mHashMap: HashMap<String, RequestBody>, @Part file: MultipartBody.Part): Call<AddCareReceiverResponse>

    @DELETE("careReceivers/{id}")
    fun deleteCareReceiver(@Path("id") itemId: String): Call<DeleteCareReceiverResponse>

    @GET("countries/getByCountryCode/{id}")
    fun fetchLanguages(@Path("id") id: String): Call<LanguageResponse>

}
