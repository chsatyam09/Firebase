package com.example.firebaselearning

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.firebaselearning.navigation.NavigationGraph
import com.example.firebaselearning.ui.theme.FirebaseLearningTheme
import com.example.firebaselearning.viewmodal.AuthViewModel
import com.example.firebaselearning.viewmodal.GoogleAuthClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException


//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            FirebaseLearningTheme {
//                val googleAuthClient = GoogleAuthClient(this)
//                val googleSignInClient = googleAuthClient.googleSignInClient
//
//                val navController = rememberNavController()
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                  NavigationGraph(
//                      navController = navController,
//                      modifier = Modifier.padding(innerPadding)
//                  )
//                }
//            }
//        }
//    }
//}

class MainActivity : ComponentActivity() {

    private val viewModel: AuthViewModel by viewModels()
    private lateinit var googleSignInClient: GoogleSignInClient

    // Activity result launcher for Google Sign-In
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account.idToken?.let { idToken ->
                viewModel.firebaseAuthWithGoogle(idToken)
            }
        } catch (e: ApiException) {
            Log.w("GoogleSignIn", "Google sign in failed", e)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val googleAuthClient = GoogleAuthClient(this)
        googleSignInClient = googleAuthClient.googleSignInClient

        setContent {
            FirebaseLearningTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigationGraph(
                        navController = navController,
                        onGoogleSignIn = {
                            val signInIntent = googleSignInClient.signInIntent
                            launcher.launch(signInIntent)
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}



