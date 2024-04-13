package com.xperiencelabs.armenu

import android.content.ContentValues.TAG
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

var output: String =""


class MLPredictorScreen() : ComponentActivity() {

      var output: String =""


      override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            setContent {
                  UploadImageScreen()
            }
      }
}


@Composable
fun UploadImageScreen() {
      var selectedImageBitmap by remember { mutableStateOf<Bitmap?>(null) }

      Column(
            modifier = Modifier
                  .fillMaxSize()
                  .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
      ) {
            if (selectedImageBitmap != null) {
                  Image(
                        bitmap = selectedImageBitmap!!.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.size(200.dp)
                  )
                  Spacer(modifier = Modifier.height(16.dp))
                  UploadButton(selectedImageBitmap!!)
                  Spacer(modifier = Modifier.height(16.dp))
                  Text(text ="Hello bhai aur kya hal chal" )
            } else {
                  PickImageButton(onImagePicked = { bitmap ->
                        selectedImageBitmap = bitmap
                  })
            }
      }
}

@Composable
fun PickImageButton(onImagePicked: (Bitmap) -> Unit) {
      val context = LocalContext.current
      val getContent = rememberLauncherForActivityResult(contract = GetContent()) { uri ->
            uri?.let {
                  val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                  onImagePicked(bitmap)
            }
      }

      Button(onClick = { getContent.launch("image/*") }) {
            Text("Pick Image")
      }
}


@Composable
fun UploadButton(bitmap: Bitmap) {
      Button(
            onClick = {
                  // Launch a coroutine to perform the upload operation
                  GlobalScope.launch(Dispatchers.IO) {
                        uploadImage(bitmap)
                  }
            },
            modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 16.dp)
      ) {
            Text("Upload Image")
      }
}

private suspend fun uploadImage(bitmap: Bitmap) {
      // Perform the upload operation here, for example, using a network call
      // For demonstration purposes, we'll simply print a message
      println("Uploading image...")
      val generativeModel = GenerativeModel(
            // Use a model that's applicable for your use case (see "Implement basic use cases" below)
            modelName = "gemini-pro-vision",
            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            apiKey = "AIzaSyDx-iWa5DukvIjFSz4PLyfIvn9_uvEcqLM"
      )

      val image1: Bitmap = bitmap


      val inputContent = content {
            image(image1)

            text("You are an expert in nutritionist where you need to see the food items from the image " +
                "and calculate the total calories, fat content and protien content , carbohydrates content, " +
                "vitamin content, also provide the details of every food items with calories intake" +
                "  is below format" +
                "               1. Item 1 - no of calories, fat content, protien content, carbohydrates content, vitamin content, " +
                "               2. Item 2 - no of calories, fat content, protien content, carbohydrates content, vitamin content, "
                )
      }

      val response = generativeModel.generateContent(inputContent)
      print(response.text)
      //Log.println(TAG response)
      Log.d(TAG, "${response.text}")
      output= response.text.toString()

      // Simulate a delay to mimic network request


      println("Image uploaded successfully!")
}