package com.logicielhouse.ca.source.remote

interface ApiCaller {


   /* @FormUrlEncoded
    @POST("api/register")
    @Headers("Accept: application/json")
    fun registerUser(
        @Field("role_id") role_id: Int,
        @Field("mobile") mobile: String,
        @Field("email") email: String,
        @Field("username") userName: String,
        @Field("password") password: String,
        @Field("confirm_password") confirmPassword: String,
        @Field("fcm_token") fcmToken: String
    ): Call<RegisterModel>


    @FormUrlEncoded
    @POST("api/login")
    fun loginUser(
        @Field("role_id") role_id: Int,
        @Field("mobile") mobile: String,
        @Field("password") password: String,
        @Field("fcm_token") fcmToken: String
    ): Call<RegisterModel>

    @FormUrlEncoded
    @POST("api/mobile_otp/send")
    @Headers("Accept: application/json")
    fun sendOTP(@Field("mobile") mobile: String): Call<OtpMainModel>

    @FormUrlEncoded
    @POST("api/mobile_otp/confirm")
    @Headers("Accept: application/json")
    fun verifyOTP(@Field("id") id: Int, @Field("otp") otp: String): Call<OtpMainModel>


    @Multipart
    @POST("api/driver/car_and_cnic_registration")
    fun uploadAllDocuments(
        @Part("user_id") user_id: RequestBody,
        @Part("cnic") cnic: RequestBody,
        @Part("driver_license") driver_license: RequestBody,
        @Part cnic_pic_front: MultipartBody.Part,
        @Part cnic_pic_back: MultipartBody.Part,
        @Part("vehicle_type_id") vehicle_type_id: RequestBody,
        @Part("vehicle_model_id") vehicle_model_id: RequestBody,
        @Part("vehicle_color_id") vehicle_color_id: RequestBody,
        @Part("registration_number") registration_number: RequestBody,
        @Part vehicle_pics: ArrayList<MultipartBody.Part>,
        @Part("fuel_type") fuel_type: RequestBody,
        @Part("manufacturing_date") manufacturing_date: RequestBody,
        @Part("registration_date") registration_date: RequestBody
    )
            : Call<CarRegResponseModel>

    @GET("api/driver/get_car_detail_list")
    fun getCarDetails(): Call<CarDetailsMainModel>

    @FormUrlEncoded
    @POST("api/driver/get_profile")
    fun getProfile(@Field("user_id") userId: Int): Call<ProfileMainModel>

    @Multipart
    @POST("api/driver/set_profile")
    fun setProfile(
        @Part("user_id") user_id: RequestBody,
        @Part("username") username: RequestBody?,
        @Part("email") email: RequestBody?,
        @Part profile_pic: MultipartBody.Part?,
        @Part("previous_password") previous_password: RequestBody?,
        @Part("new_password") new_password: RequestBody?

    )
            : Call<ProfileMainModel>

    @FormUrlEncoded
    @POST("api/driver/driver_complaint")
    fun sendSupportMail(
        @Field("user_id") user_id: Int,
        @Field("title") title: String,
        @Field("body") body: String
    )
            : Call<SupportMainModel>

    @FormUrlEncoded
    @POST("api/driver/forgot_password_send_otp")
    fun sendForgotOTP(@Field("mobile") mobile: String): Call<OtpMainModel>

    @FormUrlEncoded
    @POST("api/driver/forgot_password_send_new_pass")
    fun setPassword(
        @Field("mobile") mobile: String,
        @Field("new_password") password: String
    ): Call<NewPasswordResponse>
*/
}
