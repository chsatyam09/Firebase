package com.example.firebaselearning.screens.auth

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.firebaselearning.R
import com.example.firebaselearning.databse.local.saveCredentials
import com.example.firebaselearning.navigation.NavRoutes
import com.example.firebaselearning.viewmodal.AuthState
import com.example.firebaselearning.viewmodal.AuthViewModel
import com.jihan.vecto.Vecto
import com.jihan.vecto._vectooutlinedicons.Eye
import com.jihan.vecto._vectooutlinedicons.EyeOff
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun Login(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    val authState by authViewModel.authState.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }


    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                if (rememberMe) {
                    saveCredentials(context, email, password)
                }
                navController.navigate(NavRoutes.HomeScreen.routes) {
                    popUpTo(NavRoutes.Login.routes) { inclusive = true }
                }
                authViewModel.resetState()
            }

            is AuthState.Failure -> {
                Toast.makeText(context, (authState as AuthState.Failure).message, Toast.LENGTH_LONG).show()
                authViewModel.resetState()
            }

            else -> {}
        }
    }

    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 38.dp),
                horizontalAlignment = Alignment.Start
            ) {
                EmailInput(email = email, onEmailChange = { email = it })
                Spacer(modifier = Modifier.height(8.dp))

                PasswordInput(password = password, onPasswordChange = { password = it })
                Spacer(modifier = Modifier.height(4.dp))

                PasswordNote(password)
                Spacer(modifier = Modifier.height(8.dp))

                RememberMeAndForgot(
                    navController = navController,
                    isChecked = rememberMe,
                    onCheckedChange = { rememberMe = it }
                )
                Spacer(modifier = Modifier.height(16.dp))

                LoginButton(    email = email.trim(),
                    password = password.trim(),
                    onLoginClick = {
                        authViewModel.login(email.trim(), password.trim())
                    })

            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                GoogleLoginButton()
                Spacer(modifier = Modifier.height(16.dp))
                SignUpText(
                    navController = navController
                )
            }
        }
    }
}




@Composable
fun EmailInput(email: String, onEmailChange: (String) -> Unit) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        label = { Text("Email ") },
        placeholder = { Text("Please enter your email address .") },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .height(70.dp),
        trailingIcon = {
            if (email.isNotEmpty()) {
                IconButton(onClick = { onEmailChange("") }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                }
            }
        },
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            autoCorrect = true
        ),
        keyboardActions = KeyboardActions(onNext = {
            focusManager.moveFocus(FocusDirection.Down)
        })
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}



@Composable
fun PasswordInput(password: String, onPasswordChange: (String) -> Unit) {
    var passwordVisible by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text("Password") },
        placeholder = { Text("Please enter your password") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        maxLines = 1,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            if (password.isNotEmpty()) {
                val icon = if (passwordVisible) Vecto.Outlined.Eye else Vecto.Outlined.EyeOff
                val description = if (passwordVisible) "Hide password" else "Show password"
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = icon, contentDescription = description)
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            autoCorrect = false,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        )
    )
}


@Composable
fun PasswordNote(password: String) {
    if (password.isNotEmpty() && password.length < 8)
        if (password.length < 8) {
            Text(
                text = "Password must be at least 8 characters long",
                style = MaterialTheme.typography.titleSmall,
                color = Color.Red
            )
        } else {
            Text(
                text = "Password strength is acceptable",
                style = MaterialTheme.typography.titleSmall,
                color = Color.Gray
            )
        }
    }


@Composable
fun RememberMeAndForgot(
    navController: NavHostController,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = isChecked, onCheckedChange = onCheckedChange)
            Text("Remember me")
        }
        TextButton(
            onClick = {
                navController.navigate(NavRoutes.ForgetPassword.routes)
            },
            modifier = Modifier
        ) {
            Text(
                text = "Forgot Password",
                color = Color(0xFF2F4F4F),
                style = MaterialTheme.typography.bodySmall
            )
        }

    }
}






//@Composable
//fun LoginButton(
//    navController: NavHostController,
//    context: Context,
//    email: String,
//    password: String,
//    rememberMe: Boolean
//) {
//    val isFormValid = email.isNotBlank() && password.length >= 8
//
//    Button(
//        onClick = {
//            if (isFormValid) {
//                navController.navigate(NavRoutes.HomeScreen.routes)
//                if (rememberMe) {
//                    saveCredentials(context, email, password)
//                }
//            }
//        },
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(48.dp),
//        enabled = isFormValid,
//        colors = ButtonDefaults.buttonColors(
//            containerColor = if (isFormValid) MaterialTheme.colorScheme.primary else Color.LightGray
//        )
//    ) {
//        Text("Login")
//    }
//}

@Composable
fun LoginButton(
    email: String,
    password: String,
    onLoginClick: () -> Unit
) {
    val isFormValid = email.isNotBlank() && password.length >= 8

    Button(
        onClick = {
            if (isFormValid) {
                onLoginClick()
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        enabled = isFormValid,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isFormValid) MaterialTheme.colorScheme.primary else Color.LightGray
        )
    ) {
        Text("Login")
    }
}



@Composable
fun GoogleLoginButton() {
    OutlinedButton(
        onClick = { /* Handle Google login */ },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Google icon",
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("Login with Google")
    }
}

@Composable
fun SignUpText(navController: NavHostController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text("Don't have an account?")
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Sign Up",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
navController.navigate(NavRoutes.Signup.routes)
            }

        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        Login(  navController = rememberNavController(), modifier = Modifier)
    }
}