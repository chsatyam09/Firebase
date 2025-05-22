package com.example.firebaselearning.viewmodal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp

//OTPView composable – full OTP input view (e.g., 6 boxes).
@Composable
fun OTPView(
    modifier: Modifier = Modifier,
    otpLength: Int = 6,
    onOtpComplete: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val focusRequesters = remember { List(otpLength) { FocusRequester() } }
    var otpValues by remember { mutableStateOf(List(otpLength) { "" }) }
    var isOtpComplete by remember { mutableStateOf(false) }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        otpValues.forEachIndexed { index, otpValue ->
            OTPInput(
                otp = otpValue.toIntOrNull(),
                focusRequester = focusRequesters[index],
                onNumberChanged = { newNumber ->
                    otpValues = otpValues.toMutableList().apply {
                        this[index] = newNumber?.toString().orEmpty()
                    }
                    if (newNumber != null && index < otpLength - 1 && otpValues[index + 1].isEmpty()) {
                        focusRequesters[index + 1].requestFocus()
                    }
                    if (otpValues.all { it.isNotEmpty() }) {
                        isOtpComplete = true
                        onOtpComplete(otpValues.joinToString(separator = ""))
                    } else {
                        isOtpComplete = false
                    }
                },
                onKeyboardBack = {
                    if (index > 0) {
                        focusRequesters[index - 1].requestFocus()
                    }
                },
                onFocusChanged = { isFocused ->

                },
                isOtpComplete = isOtpComplete
            )
        }
    }

    LaunchedEffect(Unit) {
        if (!isOtpComplete) {
            focusRequesters.firstOrNull()?.requestFocus()
        }
    }

}