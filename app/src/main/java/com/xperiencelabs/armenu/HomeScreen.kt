package com.xperiencelabs.armenu

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.xperiencelabs.armenu.ui.theme.ARMenuTheme


class HomeScreen : ComponentActivity() {
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
      val context= LocalContext.current
      Box(modifier = Modifier.fillMaxSize()) {
            // Load image from resources and set it as background
            Image(
                  painterResource(id = R.drawable.background),
                  contentDescription = null,
                  modifier = Modifier.fillMaxSize(),
                  contentScale = ContentScale.FillBounds
            )
                  // Column with cards
                  Column(
                        modifier = Modifier
                              .fillMaxSize()
                              .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                  ) {
                        CardOption("AR Screen") {
                              val intent = Intent(context, MainActivity::class.java)
                              context.startActivity(intent)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        CardOption("ML Screen") {
                              // Start ML screen activity or do whatever you want
                        }
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