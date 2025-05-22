package com.example.firebaselearning.viewmodal

import android.view.KeyEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly



//OTPInput composable – single digit input box.

@Composable
fun OTPInput(
    modifier: Modifier = Modifier,
    otp: Int?, // Integer or Null

    onNumberChanged: (Int?) -> Unit, // Jab user kuch type karta hai ya delete karta hai, to ye function call hota hai.

    focusRequester: FocusRequester, // Ye help karta hai programmatically kisi box pe focus lane me.
    // Example: User ne pehla digit type kiya, ab agle box pe cursor le jana hai:

    onKeyboardBack: () -> Unit, // Jab user backspace dabata hai aur box already khaali hota hai, to pehle wale box pe focus bhejne ke liye ye call hota hai.
    // Example: 3rd box khaali hai aur backspace dabaya → cursor 2nd box me chala jaata hai.

    onFocusChanged: (Boolean) -> Unit,// Jab is box me focus aata hai ya chala jaata hai, to ye batata hai.

    isOtpComplete: Boolean // Ye check karta hai ki saare digits fill ho chuke hain ya nahi.
) {
    var text by remember {
        mutableStateOf(
            TextFieldValue(
                text = otp?.toString().orEmpty(),
                selection = TextRange(index = if (otp != null) 1 else 0) // Cursor ko kaha place karna hai,  wo decide kar rahe hain.
//                    Agar OTP me koi digit hai (yaani otp != null) → cursor digit ke baad lagana hai → index = 1
//                    Agar OTP null hai → field blank hai → cursor pehla position (0) pe hona chahiye
            )
        )
    }


    var isFocused by remember { mutableStateOf(false) } // isFocused: tracks if the field is currently focused.

    val focusManager = LocalFocusManager.current //     focusManager: controls focus programmatically (e.g., hide keyboard).


    Box(
        modifier = modifier
            .size(48.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = if (isFocused) Color.Black else Gray,
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = text,
            onValueChange = { newValue ->
                val newNumber = newValue.text
                if (newNumber.length <= 1 && newNumber.isDigitsOnly()) { // Ye check karta hai ki user ne 1 character se zyada toh nahi likh diya && Ye check karta hai ki jo user ne likha hai, wo sirf number (0–9) hi ho.
                    text = newValue // Ye line text state ko update karti hai. Yani field ke andar dikha raha value ab naye user input se update ho gayi.
                    onNumberChanged(newNumber.toIntOrNull()) // Ye callback parent ko batata hai ki kya digit type hua hai.
                    // Agar input empty hai (e.g. backspace dabake field khali ho gaya) → newNumber.toIntOrNull() return karega null.
                }
            },
            cursorBrush = SolidColor(Color.Black),
            singleLine = true,
            textStyle = TextStyle(
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                color = Color.Black
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number  ), // Ensures number keyboard shows.

            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .focusRequester(focusRequester)// Ye allow karta hai ki programmatically is field pe focus laya ja sake.// Jaise: agla digit type karte hi agla box auto-focus ho jaye.
                .onFocusChanged {
                    isFocused = it.isFocused // isFocused local variable update hota hai (shayad styling ke liye)
                    onFocusChanged(it.isFocused) // external callback call karta hai (UI ko update karne ke liye)
//                    it.isFocused == true → cursor is field me hai , false → cursor chala gaya
                }
                .onKeyEvent { event ->
                    if (event.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DEL && text.text.isEmpty()) {
                        onKeyboardBack()
                        return@onKeyEvent true
                    }
                    false
                },
            decorationBox = { innerBox ->
                innerBox()
                if (!isFocused && text.text.isEmpty()) {
                    Text(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(),
                        text = "",
                        textAlign = TextAlign.Center,
                        color = Gray,
                        fontSize = 20.sp
                    )
                }
            }
        )
    }
 }
