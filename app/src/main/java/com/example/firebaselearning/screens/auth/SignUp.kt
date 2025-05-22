package com.example.yourapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.firebaselearning.R
import com.example.firebaselearning.navigation.NavRoutes
import com.example.firebaselearning.viewmodal.AuthState
import com.example.firebaselearning.viewmodal.AuthViewModel
import com.jihan.vecto.Vecto
import com.jihan.vecto._vectooutlinedicons.Eye
import com.jihan.vecto._vectooutlinedicons.EyeOff
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun Signup(navController: NavController, viewModel: AuthViewModel = viewModel()) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val context = LocalContext.current
    val authState by viewModel.authState.collectAsState()

    // 🔁 Observe ViewModel auth state
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                navController.navigate(NavRoutes.VerifyGmail.routes) {
                    popUpTo(NavRoutes.Signup.routes) { inclusive = true }
                }
                viewModel.resetState()
            }
            is AuthState.Failure -> {
                Toast.makeText(context, (authState as AuthState.Failure).message, Toast.LENGTH_LONG).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column {
            FullNameInput(fullName) { fullName = it }
            Spacer(modifier = Modifier.height(16.dp))

            EmailInput(email) { email = it }
            Spacer(modifier = Modifier.height(16.dp))


            PasswordInput(newPassword, label = "New Password") { newPassword = it }
            Spacer(modifier = Modifier.height(8.dp))
            PasswordNotee(newPassword)
            Spacer(modifier = Modifier.height(16.dp))


            PasswordInput(confirmPassword, label = "Confirm Password") { confirmPassword = it }
            Spacer(modifier = Modifier.height(8.dp))
            ConfirmPasswordNote(newPassword, confirmPassword)
            Spacer(modifier = Modifier.height(16.dp))


            Spacer(modifier = Modifier.height(16.dp))

            SignUpButton(
                fullName = fullName,
                email = email,
                password = newPassword,
                confirmPassword = confirmPassword,
                onSignUp = {
                    if (newPassword == confirmPassword) {
                        viewModel.register(email, newPassword)

                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }


        Spacer(modifier = Modifier.weight(1f))


        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            SignUpWithGoogle()
            Spacer(modifier = Modifier.height(24.dp))

            LoginText(navController)
        }
    }
}


@Composable
fun FullNameInput(value: String, onValueChange: (String) -> Unit) {
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Full Name ") },
        placeholder = { Text("Please enter your Full Name ") },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .height(70.dp),
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
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
fun EmailInput(email: String, onEmailChange: (String) -> Unit) {

    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        label = { Text("Email ") },
        placeholder = { Text("Please enter your email address .") },
        modifier = Modifier
            .fillMaxWidth()
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
}

@Composable
fun PasswordInput(password: String,  label: String = "Password",onPasswordChange: (String) -> Unit) {
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
fun PasswordNotee(password: String) {
    if (password.isNotEmpty() && password.length < 8) {
        Text(
            text = "Password must be at least 8 characters long",
            style = MaterialTheme.typography.titleSmall,
            color = Color.Red
        )
    }
}

@Composable
fun ConfirmPasswordNote(password: String, confirmPassword: String) {
    if (confirmPassword.isNotEmpty() && confirmPassword != password) {
        Text(
            text = "Passwords do not match",
            style = MaterialTheme.typography.titleSmall,
            color = Color.Red
        )
    }
}

@Composable
fun SignUpButton(
    fullName: String,
    email: String,
    password: String,
    confirmPassword: String,
    onSignUp: () -> Unit
) {
    val isFormValid = fullName.isNotBlank() && email.isNotBlank() &&
            password.length >= 8 && password == confirmPassword

    Button(
        onClick = { if (isFormValid) onSignUp() },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        enabled = isFormValid
    ) {
        Text("Sign Up")
    }
}



//@Composable
//fun SignUpButton(
//    navController: NavController,
//    value: String,
//    email: String,
//    password: String
//) {
//    val isFormValid = value.isNotBlank() && email.isNotBlank() && password.length >= 8
//
//    Button(
//        onClick = {
//            if (isFormValid) {
//                navController.navigate(NavRoutes.VerifyGmail.routes)
//            }
//        },
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(50.dp),
//        enabled = isFormValid
//    ) {
//        Text("Sign Up")
//    }
//}


@Composable
fun SignUpWithGoogle() {
    OutlinedButton(
        onClick = { /* Handle Google Sign Up */ },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.img), // Make sure the drawable exists
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("Sign Up with Google")
    }
}

@Composable
fun LoginText(navController: NavController) {
    Row {
        Text("Already have an account?")
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Login here",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                 navController.navigate(NavRoutes.Login.routes)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationScreenPreview() {
    MaterialTheme {
        Signup(navController = rememberNavController(),)
    }
}
