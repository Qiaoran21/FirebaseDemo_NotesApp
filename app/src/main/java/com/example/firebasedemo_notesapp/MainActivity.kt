package com.example.firebasedemo_notesapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.firebasedemo_notesapp.ui.theme.FirebaseDemo_NotesAppTheme
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import java.util.UUID

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseDemo_NotesAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AddNote()
                }
            }
        }
    }
}

@Composable
fun AddNote() {
    val db = Firebase.firestore
    val (noteContent, setNoteContent) = remember { mutableStateOf("") }

    Column {
        TextField(
            value = noteContent,
            onValueChange = { setNoteContent(it) }
        )

        Button(
            onClick = {
                val note = hashMapOf(
                    "id" to UUID.randomUUID().toString(),
                    "content" to noteContent
                )

                db.collection("notes")
                    .add(note)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
                        setNoteContent("")
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
                    }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Add")
        }
    }
}
