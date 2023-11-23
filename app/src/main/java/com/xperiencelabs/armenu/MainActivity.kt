package com.xperiencelabs.armenu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.ar.core.Config
import com.xperiencelabs.armenu.ui.theme.ARMenuTheme
import com.xperiencelabs.armenu.ui.theme.Brown
import com.xperiencelabs.armenu.ui.theme.Purple
import com.xperiencelabs.armenu.ui.theme.Translucent
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.ArNode
import io.github.sceneview.ar.node.PlacementMode

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ARMenuTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                  Box(modifier = Modifier.fillMaxSize()){
                      val currentModel = remember {
                          mutableStateOf("burger")
                      }
                      ARScreen(currentModel.value)
                      Menu(modifier = Modifier.align(Alignment.BottomCenter)){
                          currentModel.value = it
                      }

                  }
                }
            }
        }
    }
}



@Composable
fun Menu(modifier: Modifier,onClick:(String)->Unit) {
    var currentIndex by remember {
        mutableStateOf(0)
    }
    val context = LocalContext.current

    val itemsList = listOf(
        Food("burger",R.drawable.burger),
        Food("instant",R.drawable.instant),
        Food("momos",R.drawable.momos),
        Food("pizza",R.drawable.pizza),
        Food("ramen",R.drawable.ramen),

    )
    fun updateIndex(offset:Int){
        currentIndex = (currentIndex+offset + itemsList.size) % itemsList.size
        onClick(itemsList[currentIndex].name)
    }
    Row(modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Card(modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp, // Adjust the border width as needed
                color = Color(0xFF8B4513) // Dark brown color
            ),
            backgroundColor = Brown,
            ){
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(modifier = Modifier.padding(8.dp)
                    ,horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        updateIndex(-1)
                    }) {
                        Icon(painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24), contentDescription ="previous" )
                    }

                    CircularImage(imageId = itemsList[currentIndex].imageId )

                    IconButton(onClick = {
                        updateIndex(1)
                    }) {
                        Icon(painter = painterResource(id = R.drawable.baseline_arrow_forward_ios_24), contentDescription ="next")
                    }
                }
                Button(onClick = {
                    val intent=Intent(context,MainActivity2::class.java)
                    intent.putExtra("itemName", itemsList[currentIndex].name)
                    context.startActivity(intent)
                },
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color(0xFFD2B48C), shape = RoundedCornerShape(4.dp))) {
                    Text(text = "Place Order", color = Color.White,
                        style = MaterialTheme.typography.button)
                }
            }

        }

    }

}

@Composable
fun CircularImage(
    modifier: Modifier=Modifier,
    imageId: Int,
) {
    Box(modifier = modifier
        .size(140.dp)
        .clip(CircleShape)
        .border(width = 4.dp, Color.Yellow, CircleShape)
    ){
        Image(painter = painterResource(id = imageId), contentDescription = null, modifier = Modifier.size(140.dp), contentScale = ContentScale.FillBounds)
    }
}

@Composable
fun ARScreen(model:String) {
    val nodes = remember {
        mutableListOf<ArNode>()
    }
    val modelNode = remember {
        mutableStateOf<ArModelNode?>(null)
    }
    val placeModelButton = remember {
        mutableStateOf(false)
    }
    Box(modifier = Modifier.fillMaxSize()){
        ARScene(
            modifier = Modifier.fillMaxSize(),
            nodes = nodes,
            planeRenderer = true,
            onCreate = {arSceneView ->
                arSceneView.lightEstimationMode = Config.LightEstimationMode.DISABLED
                arSceneView.planeRenderer.isShadowReceiver = false
                modelNode.value = ArModelNode(arSceneView.engine,PlacementMode.INSTANT).apply {
                    loadModelGlbAsync(
                        glbFileLocation = "models/${model}.glb",
                        scaleToUnits = 0.8f
                    ){

                    }
                    onAnchorChanged = {
                        placeModelButton.value = !isAnchored
                    }
                    onHitResult = {node, hitResult ->
                        placeModelButton.value = node.isTracking
                    }

                }
                nodes.add(modelNode.value!!)
            },
            onSessionCreate = {
                planeRenderer.isVisible = false
            }
        )
        if(placeModelButton.value){
            Button(onClick = {
                modelNode.value?.anchor()
            }, modifier = Modifier.align(Alignment.Center)) {
                Text(text = "Place It")
            }
        }
    }


LaunchedEffect(key1 = model){
    modelNode.value?.loadModelGlbAsync(
        glbFileLocation = "models/${model}.glb",
        scaleToUnits = 0.8f
    )
    Log.e("errorloading","ERROR LOADING MODEL")
}

}


data class Food(var name:String,var imageId:Int)






