package com.natife.timeforce.ui.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.natife.timeforce.ui.navigation.bottom.BottomNavItem

context (NavHostController)
@Composable
fun MainScreenBottomNavBar(modifier: Modifier, onNavigate: (String, Int) -> Unit) {
    NavigationBar(
        modifier = modifier
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
            val navBackStackEntry by currentBackStackEntryAsState()
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
                    onNavigate(it.screenRoute, it.title)
                })
        }
    }
}
