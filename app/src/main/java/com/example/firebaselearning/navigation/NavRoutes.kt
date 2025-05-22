package com.example.firebaselearning.navigation

sealed class NavRoutes(val routes: String){

    object WelcomeSplash : NavRoutes("welcome_splash_screen")
    object Login : NavRoutes("login")
    object Signup :NavRoutes("signup")
    object VerifyGmail :NavRoutes("verify_gmail")
    object CompletedSignup :NavRoutes("Completed_Signup")
    object ForgetPassword : NavRoutes("forget_password")
    object HomeScreen :NavRoutes("home_screen")


}