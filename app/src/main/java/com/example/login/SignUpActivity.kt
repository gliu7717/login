package com.example.login.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.login.databinding.ActivitySignUpBinding
import com.example.login.models.GeneralResponse
import com.example.login.utils.MySharedPreference
import com.example.login.utils.Utility
import com.example.login.utils.VolleyUtility
import com.google.gson.Gson
import java.io.ByteArrayOutputStream
import java.net.URLEncoder
import java.nio.charset.Charset

class SignUpActivity : AppCompatActivity() {
    lateinit var binding:ActivitySignUpBinding
    var encodedImage : String? = null
    private lateinit var mySharedPreference: MySharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mySharedPreference = MySharedPreference()
        setListeners()
    }

    private fun setListeners(){
        binding.tvSignIn.setOnClickListener { it -> onBackPressed()
        }
        binding.btnSignUp.setOnClickListener {
            if(isValidSignUpDetail()) {
                signUp()
            }
        }
        binding.layoutImage.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            pickImage.launch(intent)
        }
    }

    private fun showToast(message : String)
    {
        Toast.makeText(applicationContext,message, Toast.LENGTH_LONG).show()
    }

    private fun signUp() {
        loading(true)
        binding.btnSignUp.isEnabled = false
        val url = mySharedPreference.getAPIURL(this) + "/registration"
        val requestBody =
            "name=" + binding.etInputName.text +
                    "&email=" + binding.etInputEmail.text +
                    "&password=" + binding.etInputPassword.text +
                    "&password1=" + binding.etInputConfirmPassword.text
        VolleyUtility.volleyRequest(this,url, requestBody) { response: String ->
            volleyCallBack(
                response
            )
        }
    }

    private fun volleyCallBack(response:String)
    {
        binding.btnSignUp.isEnabled = true
        loading(false)
    }

    private fun encodeImage(bitmap: Bitmap) :String
    {
        val previewWidth = 150
        val previewHeight = bitmap.height* previewWidth / bitmap.width
        val previewdBitmap  = Bitmap.createScaledBitmap(bitmap,previewWidth,previewHeight,false )
        val byteArrayOutputStream = ByteArrayOutputStream()
        previewdBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream)
        Log.i("CHATAPP", Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT ) )
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT )
    }

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
            if(result.resultCode == Activity.RESULT_OK ){
                val data = result.data
                val imageUri : Uri? = data?.data
                val inputStream = imageUri?.let { contentResolver.openInputStream(it) }
                val bitmap = BitmapFactory.decodeStream(inputStream)
                binding.rvImageProfile.setImageBitmap(bitmap)
                binding.tvAddImage.visibility = View.GONE
                encodedImage =encodeImage(bitmap)
            }
            else{
                showToast("Cancelled ...")
            }
        }


    private fun isValidSignUpDetail() : Boolean
    {
//        if(encodedImage == null){
//            showToast("Select Profile Image.")
//            return false;
//        }else if(binding.etInputName.text.toString().trim().isEmpty()){
        if(binding.etInputName.text.toString().trim().isEmpty()){
            showToast("Enter Name")
            return false;
        } else if(binding.etInputEmail.text.toString().trim().isEmpty()){
            showToast("Enter Email")
            return false;
        } else if(! Patterns.EMAIL_ADDRESS.matcher( binding.etInputEmail.text.toString()).matches()){
            showToast("Enter Valid Email")
            return false;
        } else if(binding.etInputPassword.text.toString().trim().isEmpty()){
            showToast("Enter Password")
            return false;
        } else if(binding.etInputConfirmPassword.text.toString().trim().isEmpty()) {
            showToast("Confirm your Password")
            return false
        }else if( !binding.etInputPassword.text.toString().equals(binding.etInputConfirmPassword.text.toString())) {
            showToast("Password and confirm password must be same")
            return false
        }

        return true;

    }

    fun loading (isLoading :Boolean)
    {
        if(isLoading){
            binding.btnSignUp.visibility = View.INVISIBLE
            binding.progressBar.visibility = View.VISIBLE
        }
        else{
            binding.btnSignUp.visibility = View.VISIBLE
            binding.progressBar.visibility = View.INVISIBLE
        }
    }

}