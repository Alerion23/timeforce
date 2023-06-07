package com.natife.timeforce.ui.screens.mainscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.natife.timeforce.navigation.BottomNavGraph
import com.natife.timeforce.navigation.BottomNavItem
import com.natife.timeforce.ui.theme.AppTheme

@Composable
fun MainScreenNavigationView(
    navController: NavHostController
//    mainScreenNavigationViewModel: MainScreenNavigationViewModel = hiltViewModel()
) {
    AppTheme {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp))
                ) {
                    val items = listOf(
                        BottomNavItem.AlarmClock,
                        BottomNavItem.StopWatch,
                        BottomNavItem.Clock,
                        BottomNavItem.Timer
                    )
                    items.forEach {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route
                        NavigationBarItem(
                            selected = currentRoute == it.screenRoute,
                            label = {
                                Text(
                                    text = stringResource(it.title),
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = it.icon),
                                    contentDescription = stringResource(it.title)
                                )
                            },
                            onClick = {
                                navController.navigate(it.screenRoute) {
                                    navController.graph.startDestinationRoute?.let { destination ->
                                        popUpTo(destination) {
                                            saveState = true
                                        }
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            })
                    }
                }
            }
        ) {
            Box(modifier = Modifier.padding(it)) {
                BottomNavGraph(navController = navController)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    MainScreenNavigationView(rememberNavController())
}