package com.mipa.readerandroid.view.compose.base

import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mipa.readerandroid.ui.theme.ReaderAndroidTheme

@Composable
fun IconAndTextItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    iconTint: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    spacing: Dp = 8.dp,
    padding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    rippleEnabled: Boolean = true
) {
    Row(
        modifier = modifier
            .clickable(
//                interactionSource = remember { MutableInteractionSource() },
//                indication = if (rippleEnabled) LocalRippleTheme.current else null,
                onClick = onClick
            )
            .fillMaxWidth()
            .padding(padding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = iconTint,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.width(spacing))
        Text(
            text = text,
            style = textStyle,
            fontSize = 24.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(widthDp = 360, showBackground = true)
@Composable
fun preview(){
    IconAndTextItem(Icons.Filled.AddComment, "comment")
}