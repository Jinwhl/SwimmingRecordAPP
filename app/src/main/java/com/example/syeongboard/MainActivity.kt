package com.example.syeongboard

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.syeongboard.compose.AppNavigator
import com.example.syeongboard.ui.theme.SyeongBoardTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SyeongBoardTheme {
                //TestScreen()
                AppNavigator()
                //GoogleSignInButton()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
fun MainPreview() {
    SyeongBoardTheme {
        AppNavigator()
    }
}

