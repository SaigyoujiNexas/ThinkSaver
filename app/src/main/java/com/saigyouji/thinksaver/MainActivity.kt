package com.saigyouji.thinksaver

import com.saigyouji.thinksaver.logic.State
import com.saigyouji.thinksaver.logic.Thought
import com.saigyouji.thinksaver.logic.ThoughtViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.saigyouji.thinksaver.ui.theme.ThinkSaverTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this)[ThoughtViewModel::class.java]
        viewModel.init(this)
        setContent {
            ThinkSaverTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val state by viewModel.state.observeAsState()
                    when(state){
                        State.First -> Menu(viewModel = viewModel)
                        is State.Second -> {
                            Content((state as State.Second).thought, viewModel = viewModel)
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}

@Composable
fun Menu(viewModel: ThoughtViewModel){
    val thoughts by viewModel.thoughts.observeAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.openContent(Thought(topic = "", content = ""))
            }) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)

            }
        }
    ) {
        LazyColumn(
            modifier = Modifier.padding(it)
        ){
            items(thoughts?: emptyList()){
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 24.dp)
                    .heightIn(min = 32.dp), shape = RoundedCornerShape(7.dp), elevation = 5.dp) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(it.topic, modifier = Modifier
                            .clickable {
                                viewModel.openContent(it)
                            }
                            .align(Alignment.CenterVertically)
                            .padding(start = 7.dp)
                            .weight(1f))
                        IconButton(onClick = {
                            viewModel.deleteThought(it)
                        },
                        modifier = Modifier.align(Alignment.Top)) {
                            Icon(imageVector = Icons.Filled.Delete, contentDescription = null,)
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun Content(thought: Thought, viewModel: ThoughtViewModel){
    var topic by remember {
        mutableStateOf(thought.topic)
    }
    var content by remember {
        mutableStateOf(thought.content)
    }
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(value = topic, onValueChange = {topic = it}, label = {
            Text("topic")
        })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = content, onValueChange = {content = it},
            label = {Text("content")},
            modifier = Modifier.weight(1F))
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            thought.content = content
            thought.topic = topic
            viewModel.saveThought(thought)
            viewModel.back()
        }){
            Text("save")
        }
    }
    BackHandler(enabled = true) {
        viewModel.back()
    }
}


