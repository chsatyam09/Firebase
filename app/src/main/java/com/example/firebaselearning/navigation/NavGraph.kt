package com.example.firebaselearning.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.firebaselearning.screens.auth.CompletedSignup
import com.example.firebaselearning.screens.auth.ForgotPassword
import com.example.firebaselearning.screens.auth.HomeScreen
import com.example.firebaselearning.screens.auth.Login
import com.example.firebaselearning.screens.auth.VerifyGmail
import com.example.firebaselearning.screens.auth.WelcomeSplash
import com.example.yourapp.ui.screens.Signup

@Composable
fun NavigationGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = NavRoutes.WelcomeSplash.routes, modifier = modifier) {

        composable(NavRoutes.WelcomeSplash.routes) {
            WelcomeSplash(navController)

        }
        composable(NavRoutes.Login.routes) {
            Login(navController)
        }

        composable(NavRoutes.ForgetPassword.routes) { backStackEntry ->
            val emailState = rememberSaveable { mutableStateOf("") }

            ForgotPassword(
                navController = navController,
                email = emailState.value,
                onEmailChange = { emailState.value = it }
            )
        }


        composable(NavRoutes.Signup.routes) {
           Signup(navController)
        }
        composable(NavRoutes.VerifyGmail.routes) {
            VerifyGmail(navController)
        }
//        composable("verify_gmail?email={email}") { backStackEntry ->
//            val email = backStackEntry.arguments?.getString("email") ?: ""
//            VerifyGmail(navController = navController, email = email)
//        }

        composable(NavRoutes.CompletedSignup.routes) {
           CompletedSignup(navController)
        }
        composable(NavRoutes.HomeScreen.routes) {
            HomeScreen(navController)
        }
    }
}


