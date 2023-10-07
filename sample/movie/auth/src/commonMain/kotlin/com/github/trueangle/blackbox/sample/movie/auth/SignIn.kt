package com.github.trueangle.blackbox.sample.movie.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.github.trueangle.blackbox.sample.movie.design.MainButton
import com.github.trueangle.blackbox.sample.movie.design.supportWideScreen

@Composable
fun SignIn(
    onSubmit: () -> Unit,
    onClose: () -> Unit
) {
    Scaffold(
        topBar = {
            SignInSignUpTopAppBar(
                topAppBarText = "Sign in",
                onNavUp = onClose,
            )
        },
        content = { contentPadding ->
            LazyColumn(
                modifier = Modifier.supportWideScreen(),
                contentPadding = contentPadding
            ) {
                item {
                    Spacer(modifier = Modifier.height(44.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    ) {
                        SignInContent(onSubmit = onSubmit)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SignInSignUpTopAppBar(
    topAppBarText: String,
    onNavUp: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = topAppBarText,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavUp) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(0.6F)
                )
            }
        },
        actions = {
            Spacer(modifier = Modifier.width(68.dp))
        },
    )
}

@Composable
private fun SignInContent(
    onSubmit: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val focusRequester = remember { FocusRequester() }

        Email(onImeAction = { focusRequester.requestFocus() })

        Spacer(modifier = Modifier.height(16.dp))

        Password(
            label = "Password",
            modifier = Modifier.focusRequester(focusRequester),
            onImeAction = onSubmit
        )

        Spacer(modifier = Modifier.height(42.dp))

        MainButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Sign in",
            onClick = onSubmit,
        )
    }
}

@Composable
private fun Email(
    imeAction: ImeAction = ImeAction.Next,
    onImeAction: () -> Unit = {}
) {
    OutlinedTextField(
        value = "example@email.com",
        onValueChange = {

        },
        label = {
            Text(
                text = "Email",
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        modifier = Modifier
            .fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyMedium,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
            keyboardType = KeyboardType.Email
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onImeAction()
            }
        ),
    )
}


@Composable
private fun Password(
    label: String,
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: () -> Unit = {}
) {
    OutlinedTextField(
        value = "password",
        onValueChange = {
        },
        modifier = modifier
            .fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyMedium,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onImeAction()
            }
        ),
    )
}
