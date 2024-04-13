package com.xperiencelabs.armenu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.xperiencelabs.armenu.ui.theme.ARMenuTheme


class HomeScreen : AppCompatActivity() {
      override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                  ARMenuTheme {
                        Home_Screen()
                  }
            }
      }
}



@Composable
fun Home_Screen() {
      Column(
            modifier = Modifier
                  .fillMaxSize()
                  .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
      ) {
            CardOption("Main Activity 1") {
                  //val intent = Intent(context,MainActivity::class.java)
                  //startActivity(intent)
            }
            Spacer(modifier = Modifier.height(16.dp))
            CardOption("Main Activity 2") {
//                  val intent = Intent(context, MainActivity2::class.java)
//                  context.startActivity(intent)
            }
      }
}

@Composable
fun CardOption(title: String, onClick: () -> Unit) {
      Card(
            shape = RoundedCornerShape(8.dp),
            backgroundColor = Color.LightGray,
            modifier = Modifier
                  .fillMaxWidth()
                  .clickable(onClick = onClick)
                  .padding(16.dp)
      ) {
            Text(
                  text = title,
                  modifier = Modifier.padding(16.dp)
            )
      }
}