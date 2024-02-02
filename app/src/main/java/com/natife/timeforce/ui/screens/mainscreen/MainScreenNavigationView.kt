package com.natife.timeforce.ui.screens.mainscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.natife.timeforce.R
import com.natife.timeforce.navigation.BottomNavGraph
import com.natife.timeforce.navigation.BottomNavItem
import com.natife.timeforce.ui.common.MainScreenTopBar
import com.natife.timeforce.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenNavigationView(
    navController: NavHostController
//    mainScreenNavigationViewModel: MainScreenNavigationViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val topBarTitle = remember {
        mutableStateOf(R.string.alarm_title)
    }
    AppTheme {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                MainScreenTopBar(
                    scrollBehavior = scrollBehavior,
                    title = stringResource(id = topBarTitle.value)
                )
            },
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
                                topBarTitle.value = it.title
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