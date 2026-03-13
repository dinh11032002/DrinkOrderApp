package com.truongdinh.drinkorder.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.truongdinh.drinkorder.R
import com.truongdinh.drinkorder.data.model.User
import com.truongdinh.drinkorder.ui.components.AppBackground
import com.truongdinh.drinkorder.ui.components.AppHeader
import com.truongdinh.drinkorder.ui.components.ProfileItem
import com.truongdinh.drinkorder.ui.theme.AppSize
import com.truongdinh.drinkorder.ui.theme.AppSpacing

@Composable
fun ProfileContent(
    user: User,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier) {
    AppBackground {
        Scaffold(
            topBar = {
                AppHeader(title = "Thông tin nhân viên")
            },
            containerColor = Color.Transparent,
            contentWindowInsets = ScaffoldDefaults.contentWindowInsets
        ) { innerPadding ->

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(AppSpacing.md),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = AppSpacing.lg)
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = "Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(AppSize.imageLg)
                                .clip(CircleShape)
                                .border(1.dp, Color.Gray, CircleShape)
                                .align(Alignment.Center)
                        )

                        IconButton(
                            onClick = { },
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = AppSpacing.md)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(AppSpacing.lg))

                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(modifier = Modifier.height(AppSpacing.sm))

                    Text(
                        text = user.email,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(0.5f)
                    )

                    Spacer(modifier = Modifier.height(AppSpacing.lg))

                    ProfileItem( text = "Đổi mật khẩu" )
                    ProfileItem( text = "Âm thanh thông báo" )
                    ProfileItem( text = "Rung khi có order" )
                    ProfileItem( text = "Ngôn ngữ" )
                    ProfileItem( text = "Liên hệ quản lý" )
                }

                Column {

                    Button(
                        onClick = onLogout,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(AppSize.buttonLgHeight),
                        shape = RoundedCornerShape(AppSize.buttonRadius)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Logout,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(AppSpacing.sm))
                        Text("Đăng xuất")
                    }

                    Spacer(modifier = Modifier.height(AppSpacing.sm))

                    Text(
                        text = "Phiên bản 1.0.0",
                        color = Color.Gray,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}