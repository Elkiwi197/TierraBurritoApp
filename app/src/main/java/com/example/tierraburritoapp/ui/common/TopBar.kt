package com.example.tierraburritoapp.ui.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.tierraburritoapp.common.Constantes
import com.example.tierraburritoapp.ui.navigation.AppDestination


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
    screen: AppDestination?,

    ) {

    screen?.let {destination ->
        TopAppBar(

            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                titleContentColor = MaterialTheme.colorScheme.secondary,
            ),
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = destination.scaffoldState.topBarState.arrangement
                ) {
                    Text(
                        text = destination.title,
                    )
                }

            },
            navigationIcon = {
                if (destination.scaffoldState.topBarState.showNavigationIcon) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = Constantes.LOCALIZED_DESCRIPTION
                        )
                    }
                }

            },
            actions = destination.scaffoldState.topBarState.actions,
            scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
        )
    }

}