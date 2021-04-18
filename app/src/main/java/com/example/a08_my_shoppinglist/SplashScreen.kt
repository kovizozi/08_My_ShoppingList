package com.example.a08_my_shoppinglist


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Toast

import com.example.a08_my_shoppinglist.databinding.LogoLayoutBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.Exception
const val  REQUES_CODE_SIGN_IN = 0

class SplashScreen : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    private lateinit var binding: LogoLayoutBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LogoLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth=FirebaseAuth.getInstance()
        binding.tvstat.setOnClickListener{
            signInwGoogle()
        }

        checkLoggedInState()

    }

    private fun signInwGoogle(){
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.webclient_id))
            .requestEmail()
            .build()
        val signInClient = GoogleSignIn.getClient(this,options)
        signInClient.signInIntent.also {
            startActivityForResult(it, REQUES_CODE_SIGN_IN)
        }
    }

    private fun googleAuthForFirebase(account: GoogleSignInAccount){
        val credentials = GoogleAuthProvider.getCredential(account.idToken,null)
        try {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.signInWithCredential(credentials).await()
                    withContext(Dispatchers.Main) {
                        checkLoggedInState()
                        Toast.makeText(
                            this@SplashScreen,
                            "Succesfully logged in",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@SplashScreen, e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }catch (e: Exception){
            Toast.makeText(this@SplashScreen, e.message, Toast.LENGTH_LONG).show()
            System.out.println("err:${e.message}")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    if(requestCode == REQUES_CODE_SIGN_IN){
        val account=GoogleSignIn.getSignedInAccountFromIntent(data).result
        account?.let {
            googleAuthForFirebase(it)
        }
    }
    }

    private fun checkLoggedInState(){
        val user = auth.currentUser

        if(user == null){

            binding.tvstat.text ="No body logged in!"
        }else {
            System.out.println("********getIdToken:${user.getIdToken(true).toString()}")
            binding.tvstat.text = "Logged in"
            Toast.makeText(this@SplashScreen, "Already logged in",Toast.LENGTH_LONG).show()
            val scrollingintent :Intent= Intent()
            scrollingintent.setClass(this@SplashScreen, ScrollingActivity::class.java)
        startActivity(scrollingintent)
        }
    }
}

