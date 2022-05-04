package com.example.readerapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.readerapp.screens.SplashScreen
import com.example.readerapp.screens.details.ReaderBookDetailsScreen
import com.example.readerapp.screens.home.HomeScreenViewModel
import com.example.readerapp.screens.home.ReaderHomeScreen
import com.example.readerapp.screens.login.ReaderLoginScreen
import com.example.readerapp.screens.search.BooksSearchViewModel
import com.example.readerapp.screens.search.ReaderBookSearchScreen
import com.example.readerapp.screens.stats.ReaderStatsScreen
import com.example.readerapp.screens.update.ReaderBookUpdateScreen

@Composable
fun ReaderNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = ReaderScreens.SplashScreen.name) {

        composable(ReaderScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }
        composable(ReaderScreens.LoginScreen.name) {
            ReaderLoginScreen(navController = navController)
        }
        composable(ReaderScreens.ReaderHomeScreen.name) {
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            ReaderHomeScreen(navController = navController,viewModel = homeViewModel)
        }
        composable(ReaderScreens.ReaderStatsScreen.name) {
            val viewModel = hiltViewModel<HomeScreenViewModel>()
            ReaderStatsScreen(navController = navController,viewModel = viewModel)
        }
        composable(ReaderScreens.SearchScreen.name) {
            val searchViewModel = hiltViewModel<BooksSearchViewModel>()
            ReaderBookSearchScreen(navController = navController, viewModel = searchViewModel)
        }
        val detailName = ReaderScreens.DetailScreen.name
        composable("$detailName/{bookId}",
            arguments = listOf(navArgument("bookId") {
                type = NavType.StringType
            })) { backStackEntry ->
            backStackEntry.arguments?.getString("bookId").let {

                ReaderBookDetailsScreen(navController = navController, bookId = it.toString())
            }
        }

        val updateName = ReaderScreens.UpdateScreen.name
        composable("$updateName/{bookItemId}",
            arguments = listOf(navArgument("bookItemId") {
                type = NavType.StringType
            })) { navBackStackEntry ->

            navBackStackEntry.arguments?.getString("bookItemId").let {
                ReaderBookUpdateScreen(navController = navController, bookItemId = it.toString())
            }

        }

    }

}