package com.github.trueangle.blackbox.sample.movie.auth

import androidx.compose.foundation.layout.Column
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
import com.github.trueangle.blackbox.sample.movie.design.MainButton
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
            Column(
                modifier = Modifier.padding(contentPadding),
            ) {
                Spacer(modifier = Modifier.height(44.dp))

                Surface(
                    modifier = Modifier.padding(horizontal = 16.dp)
                        .clip(MaterialTheme.shapes.small),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(Modifier.fillMaxWidth().padding(16.dp)) {

                        Text(
                            modifier = Modifier,
                            text = "Name:",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            modifier = Modifier,
                            text = user.name,
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(Modifier.height(16.dp))
                        Text(
                            modifier = Modifier,
                            text = "Email:",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            modifier = Modifier,
                            text = user.email,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            modifier = Modifier,
                            text = "Phone:",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            modifier = Modifier,
                            text = user.phone,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                MainButton(
                    text = "Use the profile",
                    onClick = { onPick(user) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                )
            }
        }
    )
}
