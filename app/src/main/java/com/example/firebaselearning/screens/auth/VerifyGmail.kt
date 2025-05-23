package com.example.firebaselearning.screens.auth

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.firebaselearning.navigation.NavRoutes
import com.example.firebaselearning.screens.common.CommonHeader
import com.example.firebaselearning.viewmodal.OTPView
import com.google.firebase.auth.FirebaseAuth

//@Composable
//fun VerifyGmail(navController: NavController,
//    email: String = "beoca@gmail.com",
//    onCreateAccount: () -> Unit = {},
//    onResend: () -> Unit = {}
//) {
//    var code by remember { mutableStateOf(List(6) { "" }) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(24.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            text = "Please verify your email address",
//            fontWeight = FontWeight.Bold,
//            fontSize = 20.sp,
//            textAlign = TextAlign.Center
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Text(
//            text = "We've sent an email to $email, please enter the code below.",
//            fontSize = 14.sp,
//            color = Color.Gray,
//            textAlign = TextAlign.Center
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // Code input boxes (simplified)
//        Row(
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            code.forEachIndexed { index, char ->
//                OutlinedTextField(
//                    value = char,
//                    onValueChange = {
//                        if (it.length <= 1 && it.all { c -> c.isDigit() }) {
//                            code = code.toMutableList().apply { this[index] = it }
//                        }
//                    },
//                    singleLine = true,
//                    modifier = Modifier
//                        .width(40.dp)
//                        .height(56.dp),
//                    textStyle = LocalTextStyle.current.copy(
//                        textAlign = TextAlign.Center,
//                        fontSize = 20.sp
//                    )
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        Button(
//            onClick = onCreateAccount,
//            shape = RoundedCornerShape(50),
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(48.dp)
//        ) {
//            Text("Create Account")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text(
//            text = "Didn't see your email? Resend",
//            color = Color.Gray,
//            fontSize = 14.sp
//        )
//
//        Text(
//            text = "Resend",
//            color = MaterialTheme.colorScheme.primary,
//            fontSize = 14.sp,
//            textDecoration = TextDecoration.Underline,
//            modifier = Modifier.clickable { onResend() }
//        )
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun EmailVerificationScreenPreview() {
//    MaterialTheme {
//        VerifyGmail(navController = rememberNavController())
//    }
//}

//-------------------------------------
//@Composable
//fun VerifyGmail(
//    navController: NavController,
//) {
//    var otpCode by remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(top = 20.dp)
//    ) {
//
//            CommonHeader()
//
//
//
//        // 👇 Your main content below
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(24.dp),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = "Please verify your email address",
//                fontWeight = FontWeight.Bold,
//                fontSize = 20.sp,
//                textAlign = TextAlign.Center
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Text(
//                text = "We've sent an email to example@gamil.com, please enter the code below.",
//                fontSize = 14.sp,
//                color = Color.Gray,
//                textAlign = TextAlign.Center
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            OTPView(
//                otpLength = 6,
//                onOtpComplete = { enteredCode ->
//                    otpCode = enteredCode
//                }
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            Button(
//                onClick = {
//                    navController.navigate(NavRoutes.Login.routes)
//                },
//                shape = RoundedCornerShape(50),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(48.dp),
//                enabled = otpCode.length == 6
//            ) {
//                Text("Create Account")
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Text(
//                text = "Didn't see your email?",
//                color = Color.Gray,
//                fontSize = 14.sp
//            )
//
//            Text(
//                text = "Resend",
//                color = MaterialTheme.colorScheme.primary,
//                fontSize = 14.sp,
//                textDecoration = TextDecoration.Underline,
//                modifier = Modifier.clickable {
//                    // onResend()
//                }
//            )
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun VerifyGmailPreview() {
//    val navController = rememberNavController()
//    VerifyGmail(
//        navController = navController,
////        email = "example@gmail.com",
////        onCreateAccount = { /* Preview callback */ },
////        onResend = { /* Preview callback */ }
//    )
//}



// -----------------------------------------------------------------------------

@Composable
fun VerifyGmail(
    navController: NavController
) {
    val context = LocalContext.current
    val user = FirebaseAuth.getInstance().currentUser
    var isEmailVerified by remember { mutableStateOf(user?.isEmailVerified ?: false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var verificationSent by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // Check email verification once on screen load
        user?.reload()?.addOnCompleteListener {
            isEmailVerified = user.isEmailVerified
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp)
    ) {
        CommonHeader()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Please verify your email address",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "We've sent an email to ${user?.email ?: "your email"}, please enter the code below.",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    user?.sendEmailVerification()
                    verificationSent = true
                }
            ) {
                Text("Resend Verification Email")
            }

            if (verificationSent) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Verification email resent.",
                    color = Color.Green,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    isLoading = true
                    errorMessage = null
                    user?.reload()?.addOnCompleteListener { task ->
                        isLoading = false
                        if (task.isSuccessful) {
                            isEmailVerified = user.isEmailVerified
                            if (!isEmailVerified) {
                                errorMessage = "Email is not verified yet. Please check your inbox."
                            }
                        } else {
                            errorMessage = "Failed to check verification: ${task.exception?.message}"
                        }
                    }
                },
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Check Verification")
                }
            }

            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = Color.Red, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    navController.navigate("login")
                },
                enabled = isEmailVerified
            ) {
                Text("Continue to Login")
            }
        }
    }
}
