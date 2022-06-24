package com.example.toilet_seoul

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


class LoginActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = null
    lateinit var signInIntent: Intent
    var callbackManager : CallbackManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        //이메일 로그인 버튼
        findViewById<Button>(R.id.email_login_btn).setOnClickListener {
            //이메일 로그인 버튼 누르면 로그인 과정 실행
            signinAndSignup()
        }

        //구글계정 로그인 버튼
        findViewById<Button>(R.id.google_signin_btn).setOnClickListener {
            //구글 로그인 버튼 누르면 로그인 과정 실행
            activityLauncher.launch(signInIntent)
        }

        //페이스북계정 로그인 버튼
        findViewById<Button>(R.id.facebook_signin_btn).setOnClickListener {
            //페이스북 로그인 버튼 누르면 로그인 과정 실행
            facebookLogin()
        }

        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1046765737579-sdefstgibmgsv5nnpcfdp2iod8c1qg70.apps.googleusercontent.com")   //getString(R.string.default_web_client_id)
            .requestEmail()
            .build()
        signInIntent = GoogleSignIn.getClient(this, gso).signInIntent
        //printHashKey()
        callbackManager = CallbackManager.Factory.create()
    }

    //페이스북 로그인
    @SuppressLint("PackageManagerGetSignatures")
    fun printHashKey() {
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey = String(Base64.encode(md.digest(), 0))
                Log.i("TAG", "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e("TAG", "printHashKey()", e)
        } catch (e: Exception) {
            Log.e("TAG", "printHashKey()", e)
        }
    }
    fun facebookLogin(){
        LoginManager.getInstance()
            .logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))

        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    handleFacebookAccessToken(result?.accessToken)
                }
                override fun onCancel() {
                }
                override fun onError(error: FacebookException?) {
                }
            })
    }
    fun handleFacebookAccessToken(token : AccessToken?){
        var credential = FacebookAuthProvider.getCredential(token?.token!!)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener {
                    task ->
                if(task.isSuccessful){
                    //Login
                    moveMainPage(task.result?.user)
                }else{
                    //Show the error message
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode,resultCode,data)
    }

    //구글 로그인
    val activityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth?.signInWithCredential(credential)
                    ?.addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            //인증 성공 -> 메인 화면 이동
                            moveMainPage(task.result?.user)
                        } else {
                            //인증 실패
                            Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                        }
                    }
            } catch (e: ApiException) {
                //예외처리
            }

        }

    //이메일 계정 생성
    fun signinAndSignup() {
        auth?.createUserWithEmailAndPassword(findViewById<EditText>(R.id.email_et).text.toString(), findViewById<EditText>(R.id.password_et).text.toString())
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //아이디가 생성
                    moveMainPage(task.result?.user)
                } else if (!(task.exception?.message.isNullOrEmpty())) {
                    //아이디 생성 에러 메세지
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                } else {
                    //아이디 생성 후 로그인
                    signinEmail()
                }
            }
    }

    //이메일 계정 로그인
    fun signinEmail() {
        auth?.signInWithEmailAndPassword(findViewById<EditText>(R.id.email_et).text.toString(), findViewById<EditText>(R.id.password_et).text.toString())
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //로그인 성공
                    moveMainPage(task.result?.user)
                } else {
                    //아이디 및 패스워드 불일치 메세지
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }

    //메인화면으로 이동 => 수정필요
    fun moveMainPage(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, RealtimeDB::class.java))
        }
    }

}
