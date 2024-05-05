package az.kodcraft.workout.presentation.workoutProgress.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import az.kodcraft.core.presentation.theme.body
import az.kodcraft.core.utils.noRippleClickable
import az.kodcraft.workout.R


@Composable
fun CompleteButton(onClick: () -> Unit, buttonColor: Color, isFinished: Boolean = false) {
    Row(
        modifier = Modifier
            .noRippleClickable
            { if (isFinished.not()) onClick() }
            .clip(RoundedCornerShape(8.dp))
            .background(buttonColor)
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isFinished) {
            Spacer(modifier = Modifier.width(3.dp))
            Text(
                text = stringResource(R.string.workout_details_screen_btn_workout_is_finished),
                style = MaterialTheme.typography.body
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                painter = painterResource(id = az.kodcraft.core.R.drawable.ic_check_circle),
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = "complete workout button",
                modifier = Modifier.size(20.dp)
            )
        } else {
            Icon(
                painter = painterResource(id = az.kodcraft.core.R.drawable.ic_done),
                tint = MaterialTheme.colorScheme.onBackground,
                contentDescription = "complete workout button",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(R.string.workout_details_screen_btn_complete_workout),
                style = MaterialTheme.typography.body
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
    }
}

@Composable
fun StartOverButton(onClick: () -> Unit, buttonColor: Color) {
    Row(
        modifier = Modifier
            .noRippleClickable
            { onClick() }
            .clip(RoundedCornerShape(8.dp))
            .background(buttonColor)
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {

        Text(
            text = stringResource(R.string.workout_details_screen_btn_workout_start_over),
            style = MaterialTheme.typography.body
        )

    }
}
