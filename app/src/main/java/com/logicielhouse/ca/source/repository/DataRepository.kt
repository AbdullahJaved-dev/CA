package com.logicielhouse.ca.source.repository

import android.content.Context
import com.google.gson.Gson
import com.logicielhouse.ca.source.remote.ApiCaller
import com.logicielhouse.ca.source.remote.RetrofitClient


class DataRepository(context: Context) {
    private var context: Context? = null
    private var apiCaller: ApiCaller? = null
    private var gson: Gson = Gson()

    init {
        this.context = context
        apiCaller = RetrofitClient(context).getService()
    }

    /*fun getPolyLineOptions(url: String, getPolyLineOptions: ApiCallbacks.GetPolyLineOptions) {
        val stringRequest = StringRequest(Request.Method.GET, url, {
            GetPolyOptionsTask(getPolyLineOptions).execute(it)
        }, {
            Log.d("laksdjfa", it.toString())

        })

        var requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(stringRequest)
    }

    fun registerUser(
        role_id: Int,
        mobile: String,
        email: String,
        userName: String,
        password: String,
        confirmPassword: String,
        fcmToken: String,
        listener: ApiCallbacks.RegisterUserCallback
    ) {
        apiCaller?.registerUser(
            role_id,
            mobile,
            email,
            userName,
            password,
            confirmPassword,
            fcmToken
        )?.enqueue(object : Callback<RegisterModel> {


            override fun onFailure(call: Call<RegisterModel>, t: Throwable) {
                listener.onPayLoadError(t.message.toString())
            }

            override fun onResponse(
                call: Call<RegisterModel>,
                response: retrofit2.Response<RegisterModel>
            ) {
                val gson = Gson()
                if (response.isSuccessful) {

                    if (response.body()?.status != null && response.body()?.status?.equals("success")!!) {
                        response.body()?.let {
                            listener.registerUser(it)
                        }
                    }

                } else {
                    var registerModel =
                        gson.fromJson(response.errorBody()?.string(), RegisterModel::class.java)
                    registerModel.let {
                        listener.registerUser(it)
                    }
                }
            }

        })
    }

    fun loginUser(
        role_id: Int,
        mobile: String,
        password: String,
        fcmToken: String,
        listener: ApiCallbacks.RegisterUserCallback
    ) {
        apiCaller?.loginUser(role_id, mobile, password, fcmToken)
            ?.enqueue(object : Callback<RegisterModel> {
                override fun onFailure(call: Call<RegisterModel>, t: Throwable) {
                    listener.onPayLoadError(t.message.toString())
                }

                override fun onResponse(
                    call: Call<RegisterModel>,
                    response: retrofit2.Response<RegisterModel>
                ) {
                    val gson = Gson()
                    if (response.isSuccessful) {
                        response.body()?.let {
                            listener.registerUser(it)
                        }

                    } else {
                        val registerModel =
                            gson.fromJson(response.errorBody()?.string(), RegisterModel::class.java)
                        registerModel.let {
                            listener.registerUser(it)
                        }
                        if(registerModel != null)
                        {
                            listener.registerUser(registerModel)
                        }else{
                            response.errorBody()?.string()?.let { listener.onPayLoadError(it) }
                        }

                    }
                }

            })
    }

    fun sendOTP(mobile: String, listener: ApiCallbacks.OTPCallback) {
        apiCaller?.sendOTP(mobile)?.enqueue(object : Callback<OtpMainModel> {
            override fun onFailure(call: Call<OtpMainModel>, t: Throwable) {
                listener.onPayLoadError(t.message.toString())
            }

            override fun onResponse(
                call: Call<OtpMainModel>,
                response: retrofit2.Response<OtpMainModel>
            ) {

                if (response.isSuccessful) {
                    response.body()?.let {
                        listener.otpStatus(it)
                    }
                } else {
                    response.errorBody()?.let {
                        var otpMainModel = gson.fromJson(it.string(), OtpMainModel::class.java)
                        listener.otpStatus(otpMainModel)
                    }
                }

            }

        })
    }


    fun verifyOTP(id: Int, otp: String, listener: ApiCallbacks.OTPCallback) {
        apiCaller?.verifyOTP(id, otp)?.enqueue(object : Callback<OtpMainModel> {
            override fun onFailure(call: Call<OtpMainModel>, t: Throwable) {
                listener.onPayLoadError(t.message.toString())
            }

            override fun onResponse(
                call: Call<OtpMainModel>,
                response: retrofit2.Response<OtpMainModel>
            ) {

                response.body()?.let { listener.otpStatus(it) }
            }

        })
    }

    fun uploadAllDocuments(
        user_id: RequestBody,
        cnic: RequestBody,
        driver_license: RequestBody,
        cnic_pic_front: MultipartBody.Part,
        cnic_pic_back: MultipartBody.Part,
        vehicle_type_id: RequestBody,
        vehicle_model_id: RequestBody,
        vehicle_color_id: RequestBody,
        registration_number: RequestBody,
        vehicle_pics: ArrayList<MultipartBody.Part>,
        fuel_type: RequestBody,
        manufacturing_date: RequestBody,
        registration_date: RequestBody,
        listener: ApiCallbacks.CarRegCallback
    ) {
        apiCaller?.uploadAllDocuments(
            user_id, cnic, driver_license, cnic_pic_front, cnic_pic_back,
            vehicle_type_id, vehicle_model_id, vehicle_color_id, registration_number,
            vehicle_pics, fuel_type, manufacturing_date, registration_date
        )?.enqueue(object : Callback<CarRegResponseModel> {
            override fun onFailure(call: Call<CarRegResponseModel>, t: Throwable) {
                listener.onPayLoadError(t.message.toString())
            }

            override fun onResponse(
                call: Call<CarRegResponseModel>,
                response: retrofit2.Response<CarRegResponseModel>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { listener.carRegStatus(it) }
                } else {
                    response.errorBody()?.let { Log.d("ascdasx", it.string()) }
                }
            }
        })
    }

    fun getCarDetails(listener: ApiCallbacks.CarDetailsCallback) {
        apiCaller?.getCarDetails()?.enqueue(object : Callback<CarDetailsMainModel> {
            override fun onFailure(call: Call<CarDetailsMainModel>, t: Throwable) {
                listener.onPayLoadError(t.message.toString())
            }

            override fun onResponse(
                call: Call<CarDetailsMainModel>,
                response: retrofit2.Response<CarDetailsMainModel>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        listener.extractedCarDetails(it)
                    }
                }

            }

        })
    }

    fun getProfile(userId: Int, listener: ApiCallbacks.GetProfileCallback) {
        apiCaller?.getProfile(userId)?.enqueue(object : Callback<ProfileMainModel> {
            override fun onFailure(call: Call<ProfileMainModel>, t: Throwable) {
                listener.onPayLoadError(t.message.toString())
            }

            override fun onResponse(
                call: Call<ProfileMainModel>,
                response: retrofit2.Response<ProfileMainModel>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        listener.profileData(it)
                    }
                } else {
                    response.errorBody()?.let {
                        Log.d("alscda", it.string())
                    }
                }
            }

        })
    }

    fun setProfile(
        user_id: RequestBody,
        username: RequestBody?,
        email: RequestBody?,
        previous_password: RequestBody?,
        new_password: RequestBody?,
        profile_pic: MultipartBody.Part?,
        listener: ApiCallbacks.SetProfileCallback
    ) {
        apiCaller?.setProfile(user_id,username,email,profile_pic,previous_password,new_password)?.enqueue(object: Callback<ProfileMainModel>{
            override fun onFailure(call: Call<ProfileMainModel>, t: Throwable) {
                listener.onPayLoadError(t.message.toString())
            }

            override fun onResponse(
                call: Call<ProfileMainModel>,
                response: retrofit2.Response<ProfileMainModel>
            ) {
                var gson = Gson()
                if(response.isSuccessful)
                {
                    response.body()?.let {
                        listener.set_profile(it)
                    }
                }else{
                    response.errorBody()?.let {
                        var profileMainModel = gson.fromJson(it.string().toString(),ProfileMainModel::class.java)
                        listener.set_profile(profileMainModel)
                    }
                }
            }

        })
    }

    fun sendSupportMail(user_id: Int,
                        title: String,
                        body: String,
                        listener: ApiCallbacks.SupportCallback){

        apiCaller?.sendSupportMail(user_id,title,body)?.enqueue(object : Callback<SupportMainModel>{
            override fun onFailure(call: Call<SupportMainModel>, t: Throwable) {
                listener.onPayLoadError(t.message.toString())
            }

            override fun onResponse(
                call: Call<SupportMainModel>,
                response: retrofit2.Response<SupportMainModel>
            ) {
                if(response.isSuccessful)
                {
                    response.body()?.let {
                        listener.getSupportResponse(it)
                       if(response.body() != null){
                           listener.getSupportResponse(it)
                       }else{
                           listener.onPayLoadError("Something went wrong")
                       }
                    }
                }else{
                    val supportMainModel = gson.fromJson(response.errorBody()?.string(),SupportMainModel::class.java)
                    if(supportMainModel != null)
                    {
                        listener.getSupportResponse(supportMainModel)
                    }else{
                        val jsonObject = JSONObject(response?.errorBody()?.string())
                        listener.onPayLoadError(jsonObject.getString("message"))
                    }
                }
            }

        })
    }

    fun sendFrogotOTP(mobile: String,listener : ApiCallbacks.OTPCallback){
        apiCaller?.sendForgotOTP(mobile)?.enqueue(object : Callback<OtpMainModel>{
            override fun onFailure(call: Call<OtpMainModel>, t: Throwable) {
                listener.onPayLoadError(t.message.toString())
            }

            override fun onResponse(
                call: Call<OtpMainModel>,
                response: retrofit2.Response<OtpMainModel>
            ) {
                if(response.isSuccessful)
                {
                    response.body()?.let { listener.otpStatus(it) }
                } else {

                        var otpMainModel = gson.fromJson(response.errorBody()?.string(), OtpMainModel::class.java)
                       if(otpMainModel != null)
                       {
                           listener.otpStatus(otpMainModel)
                       }else{
                           val jsonObject = JSONObject(response?.errorBody()?.string())
                           listener.onPayLoadError(jsonObject.getString("message"))
                       }

                }
            }

        })
    }

    fun setPassword(mobile: String,password: String,listener: ApiCallbacks.NewPasswordCallback){
        apiCaller?.setPassword(mobile,password)?.enqueue(object: Callback<NewPasswordResponse>{
            override fun onFailure(call: Call<NewPasswordResponse>, t: Throwable) {
                listener.onPayLoadError(t.message.toString())
            }

            override fun onResponse(
                call: Call<NewPasswordResponse>,
                response: retrofit2.Response<NewPasswordResponse>
            ) {
                    if(response.isSuccessful)
                    {
                        response.body()?.let { listener.newPasswordResponse(it) }
                    }else{
                        response.errorBody()?.let {
                            val model = gson.fromJson(it.string(),NewPasswordResponse::class.java)
                            if(model == null)
                            {
                                val jsonObject = JSONObject(response?.errorBody()?.string())
                                listener.onPayLoadError(jsonObject.getString("message"))
                            }else{
                                listener.newPasswordResponse(model)
                            }

                        }

                    }
            }

        })
    }


    inner class GetPolyOptionsTask(listener: ApiCallbacks.GetPolyLineOptions) :
        AsyncTask<String, Void, PolylineOptions>() {

        private var listener: ApiCallbacks.GetPolyLineOptions? = null

        init {
            this.listener = listener
        }

        override fun doInBackground(vararg p0: String?): PolylineOptions? {
            val jsonObject: JSONObject
            var routes: List<List<HashMap<String?, String?>>>? =
                null

            try {
                jsonObject = JSONObject(p0[0])
                // Starts parsing data
                val helper = DirectionHelper()
                routes = helper.parse(jsonObject)
                Log.e("laskdjcas", "Executing Routes : " *//*, routes.toString()*//*)
                var points: ArrayList<LatLng?>
                var lineOptions: PolylineOptions? = null
                // Traversing through all the routes
                for (i in routes.indices) {
                    points = ArrayList()
                    lineOptions = PolylineOptions()
                    // Fetching i-th route
                    val path =
                        routes[i]
                    // Fetching all the points in i-th route
                    for (j in path.indices) {
                        val point = path[j]
                        val lat = point["lat"]!!.toDouble()
                        val lng = point["lng"]!!.toDouble()
                        val position = LatLng(lat, lng)
                        points.add(position)
                    }
                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points)
                    lineOptions.width(10f)
                    context?.let { ContextCompat.getColor(it, R.color.colorAccent) }?.let {
                        lineOptions.color(
                            it
                        )
                    }
                }
                // Drawing polyline in the Google Map for the i-th route
                lineOptions?.let { return it }
            } catch (e: Exception) {
                Log.e("asdcasd", "Exception in Executing Routes : $e")
            }
            return null

        }

        override fun onPostExecute(result: PolylineOptions?) {
            result?.let { listener?.polyLineOptions(it) }
        }
    }*/


}