package com.nikolovlazar.goodbyemoney

import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.nikolovlazar.goodbyemoney.config.routes.NavGraph
import com.nikolovlazar.goodbyemoney.features.tracker.pages.Add
import com.nikolovlazar.goodbyemoney.features.tracker.pages.Categories
import com.nikolovlazar.goodbyemoney.features.tracker.pages.Expenses
import com.nikolovlazar.goodbyemoney.features.tracker.pages.Reports
import com.nikolovlazar.goodbyemoney.features.tracker.pages.Settings
import com.nikolovlazar.goodbyemoney.features.tracker.pages.*
import com.nikolovlazar.goodbyemoney.ui.theme.GoodbyeMoneyTheme
import com.nikolovlazar.goodbyemoney.ui.theme.TopAppBarBackground
import io.sentry.compose.withSentryObservableEffect

class MainActivity : ComponentActivity() {
  @OptIn(ExperimentalMaterial3Api::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    setContent {
      GoodbyeMoneyTheme {
        //Aqui esta la carne

        var showBottomBar by rememberSaveable { mutableStateOf(true) }
        val navController = rememberNavController().withSentryObservableEffect()
        val backStackEntry by navController.currentBackStackEntryAsState()

        showBottomBar = when (backStackEntry?.destination?.route) {
          "settings/categories" -> false
          "login" -> false
          "register" -> false
          "chatbot" -> false
          else -> true
        }

        Scaffold(
          bottomBar = {
            if (showBottomBar) {
              NavOptionsTracker(backStackEntry, navController)
            }
          },
          content = { innerPadding  ->
            NavGraph(navController = navController, innerPadding )
          }
        )
      }
    }
  }
}


@Composable
fun NavOptionsTracker(backStackEntry: NavBackStackEntry? , navController: NavController){
  NavigationBar(containerColor = TopAppBarBackground) {
    NavigationBarItem(
      selected = backStackEntry?.destination?.route == "expenses",
      onClick = { navController.navigate("expenses") },
      label = {
        Text("Expenses")
      },
      icon = {
        Icon(
          painterResource(id = R.drawable.upload),
          contentDescription = "Upload"
        )
      }
    )
    NavigationBarItem(
      selected = backStackEntry?.destination?.route == "reports",
      onClick = { navController.navigate("reports") },
      label = {
        Text("Reports")
      },
      icon = {
        Icon(
          painterResource(id = R.drawable.bar_chart),
          contentDescription = "Reports"
        )
      }
    )
    NavigationBarItem(
      selected = backStackEntry?.destination?.route == "add",
      onClick = { navController.navigate("add") },
      label = {
        Text("Add")
      },
      icon = {
        Icon(
          painterResource(id = R.drawable.add),
          contentDescription = "Add"
        )
      }
    )
    NavigationBarItem(
      selected = backStackEntry?.destination?.route?.startsWith("settings")
        ?: false,
      onClick = { navController.navigate("settings") },
      label = {
        Text("Settings")
      },
      icon = {
        Icon(
          painterResource(id = R.drawable.settings_outlined),
          contentDescription = "Settings"
        )
      }
    )
    NavigationBarItem(
      selected = backStackEntry?.destination?.route?.startsWith("chatbot")
        ?: false,
      onClick = { navController.navigate("chatbot") },
      label = {
        Text("ChatBot")
      },
      icon = {
        Icon(
          painterResource(id = R.drawable.chatbot),
          contentDescription = "Chatbot"
        )
      }
    )
  }

}

@Composable
fun Greeting(name: String) {
  Text(text = "Hello $name!")
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DefaultPreview() {
  GoodbyeMoneyTheme {
    Surface {
      Greeting("Android")
    }
  }
}