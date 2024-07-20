package az.kodcraft.client.presentation.clientDetails.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import az.kodcraft.client.domain.model.WorkoutListItemDm
import az.kodcraft.client.domain.model.WorkoutsFilterReqDm
import az.kodcraft.core.presentation.bases.BasePreviewContainer
import az.kodcraft.core.presentation.composable.search.SearchExplore
import az.kodcraft.core.presentation.theme.AccentBlue
import az.kodcraft.core.presentation.theme.Gray50
import az.kodcraft.core.presentation.theme.PrimaryTurq
import az.kodcraft.core.presentation.theme.bodyLarge
import az.kodcraft.core.presentation.theme.bodySmall
import az.kodcraft.core.utils.noRippleClickable


@Composable
fun WorkoutSearchBottomSheetContent(
    onFilterTagClicked: (WorkoutsFilterReqDm.WorkoutTagDm) -> Unit = {},
    onWorkoutCLicked: (WorkoutListItemDm) -> Unit = {},
    onSearchValueChanged: (String) -> Unit = {},
    workouts: List<WorkoutListItemDm>,
    onResetFilter: () -> Unit = {},
    isLoading:Boolean = false,
    filter: WorkoutsFilterReqDm
) {
    LaunchedEffect(key1 = Unit) {
        onResetFilter()
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        SearchExplore(
            filter.search,
            onValueChange = onSearchValueChanged
        )
        Spacer(modifier = Modifier.height(12.dp))
        FilterTags(
            modifier = Modifier.padding(vertical = 6.dp),
            filter.tags,
            onTabClicked = onFilterTagClicked
        )
        Box(Modifier.fillMaxWidth().weight(1f)) {
            LazyColumn(modifier = Modifier.padding(4.dp)) {
                itemsIndexed(workouts) { index, it ->
                    Box(modifier = Modifier
                        .noRippleClickable { onWorkoutCLicked(it) }
                        .padding(vertical = 10.dp)) {
                        Text(
                            text = it.name,
                            style = bodyLarge,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                    if (index != workouts.size - 1)
                        HorizontalDivider(
                            color = AccentBlue,
                            modifier = Modifier.height(0.5.dp)
                        )
                }
            }
            if(isLoading)
                CircularProgressIndicator(color = PrimaryTurq, modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun FilterTags(
    modifier: Modifier = Modifier,
    tags: List<WorkoutsFilterReqDm.WorkoutTagDm>,
    onTabClicked: (WorkoutsFilterReqDm.WorkoutTagDm) -> Unit
) {
    LazyRow(modifier = modifier) {
        items(tags) { tag ->
            FilterTagItem(
                tag.name,
                onTabClicked = { onTabClicked(tag) },
                isSelected = tag.isSelected
            )
        }
    }
}


@Composable
fun FilterTagItem(text: String, onTabClicked: () -> Unit, isSelected: Boolean) {
    Box(
        modifier = Modifier
            .padding(vertical = 2.dp, horizontal = 5.dp)
            .noRippleClickable { onTabClicked() }
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) PrimaryTurq.copy(0.7f) else Gray50),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
            text = text,
            style = bodySmall.copy(
                color = if (isSelected) Color.White else Color.Black,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        )
    }
}

@Composable
@Preview
fun Preview() = BasePreviewContainer {
    WorkoutSearchBottomSheetContent(
        filter =
        WorkoutsFilterReqDm(
            search = "", tags =
            listOf(
                WorkoutsFilterReqDm.WorkoutTagDm(
                    id = "",
                    name = "Pull"
                ), WorkoutsFilterReqDm.WorkoutTagDm(
                    id = "",
                    name = "Push"
                ), WorkoutsFilterReqDm.WorkoutTagDm(
                    id = "",
                    name = "Lower Body",
                    isSelected = true
                )
            )
        ),
        workouts = listOf(
            WorkoutListItemDm("", "Glute Focused Legs"),
            WorkoutListItemDm("", "Back and Biceps"),
            WorkoutListItemDm("", "Push day Template 1")
        ),
    )
}