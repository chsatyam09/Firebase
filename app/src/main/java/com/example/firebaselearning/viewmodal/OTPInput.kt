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
    otp: Int?,

    onNumberChanged: (Int?) -> Unit,

    focusRequester: FocusRequester,


    onKeyboardBack: () -> Unit,
    onFocusChanged: (Boolean) -> Unit,
) {
    var text by remember {
        mutableStateOf(
            TextFieldValue(
                text = otp?.toString().orEmpty(),
                selection = TextRange(index = if (otp != null) 1 else 0)
            )
        )
    }


    var isFocused by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current


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
                if (newNumber.length <= 1 && newNumber.isDigitsOnly()) { 
                    onNumberChanged(newNumber.toIntOrNull())
                }
            },
            cursorBrush = SolidColor(Color.Black),
            singleLine = true,
            textStyle = TextStyle(
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                color = Color.Black
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number  ),

            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    isFocused = it.isFocused
                    onFocusChanged(it.isFocused)
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
