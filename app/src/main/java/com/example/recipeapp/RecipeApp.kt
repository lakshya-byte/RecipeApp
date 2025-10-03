package com.example.recipeapp

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeApp() {
    val navController = rememberNavController()
    val recipeViewModel: MainViewModel = viewModel()
    val viewState by recipeViewModel.categorieState.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    AnimatedContent(targetState = currentRoute, label = "") {
                        route ->
                        Text(
                            text = if (route == Screen.RecipeScreen.route) "Recipe App" else recipeViewModel.categorieState.value.list.find { it.strCategory == navBackStackEntry?.arguments?.getString("cat") }?.strCategory ?: "",
                            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0D0D0D),
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    AnimatedContent(targetState = currentRoute, label = "") {
                        route ->
                        if (route != Screen.RecipeScreen.route) {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            )
        },
        containerColor = Color(0xFF0D0D0D)
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(
                navController = navController, startDestination = Screen.RecipeScreen.route
            ) {
                composable(Screen.RecipeScreen.route) {
                    RecipeScreen(viewstate = viewState, navigateToDetail = {
                        navController.currentBackStackEntry?.savedStateHandle?.set("cat", it)
                        navController.navigate(Screen.CategoryDetailScreen.route)
                    })
                }
                composable(Screen.CategoryDetailScreen.route)
                {
                    val category =
                        navController.previousBackStackEntry?.savedStateHandle?.get<Category>("cat")
                            ?: Category("", "", "", "")

                    CategoryDetailScreen(category)
                }
            }
        }
    }
}