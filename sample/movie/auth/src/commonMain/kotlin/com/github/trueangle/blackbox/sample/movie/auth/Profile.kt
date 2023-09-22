package com.github.trueangle.blackbox.sample.movie.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.github.trueangle.blackbox.sample.movie.core.domain.model.User
import com.github.trueangle.blackbox.sample.movie.design.supportWideScreen

@Composable
fun Profile(
    modifier: Modifier,
    onClose: () -> Unit,
    onPick: (User) -> Unit
) {
    val user = remember {
        User(1, "John Doe", "john@example.com", "+1 (123) 456-7890")
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            SignInSignUpTopAppBar(
                topAppBarText = "Profile",
                onNavUp = onClose,
            )
        },
        content = { contentPadding ->
            LazyColumn(
                modifier = Modifier.supportWideScreen(),
                contentPadding = contentPadding
            ) {
                item { Spacer(modifier = Modifier.height(44.dp)) }
                item {
                    ProfileEntry(modifier = Modifier, text = user.name)
                    Spacer(Modifier.height(16.dp))
                }
                item {
                    ProfileEntry(modifier = Modifier, text = user.email)
                    Spacer(Modifier.height(16.dp))
                }
                item {
                    ProfileEntry(modifier = Modifier, text = user.phone)
                    Spacer(Modifier.height(16.dp))
                }

                item { Spacer(Modifier.height(86.dp)) }

                item {
                    Button(
                        onClick = { onPick(user) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp)
                    ) {
                        Text(text = "Use the profile")
                    }
                }
            }
        }
    )
}

@Composable
private fun ProfileEntry(modifier: Modifier, text: String) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.clip(MaterialTheme.shapes.small).fillMaxWidth().padding(16.dp)
    ) {
        Text(text = text, style = MaterialTheme.typography.titleLarge)
    }
}