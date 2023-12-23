package com.example.login.utils

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.login.models.GeneralResponse
import com.google.gson.Gson
import java.nio.charset.Charset

object VolleyUtility {
    fun volleyRequest( context: Context,
                       url: String,
                       request:String,
                       callBack:(String)->Unit)
    {
        val queue = Volley.newRequestQueue(context)
        val stringReq: StringRequest =
            object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    callBack(response)
                    val generalResponse = Gson().fromJson(response, GeneralResponse::class.java)
                    if (generalResponse.status == "success") {
                        Utility.showAlert(context,
                            "Registered",
                            generalResponse.message
                        )
                    } else {
                        Utility.showAlert(context, "Error", generalResponse.message)
                    }
                },
                Response.ErrorListener { error ->
                    Log.i("myLog", "error = " + error)
                    Utility.showAlert(context, "Error", "error = " + error)

                }
            ) {
                override fun getBody(): ByteArray {
                    return request.toByteArray(Charset.defaultCharset())
                }
            }
        queue.add(stringReq)
    }
}