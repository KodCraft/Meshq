package az.kodcraft.workout.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class WorkoutDm(
    val id: String,
    val title: String,
    val isFinished: Boolean,
    val date: Date,
    val notes: String,
    val exercises: List<Exercise> = emptyList()
) : Parcelable {
    @Parcelize
    data class Exercise(
        val id: String,
        val exerciseRef: String,
        val name: String,
        val sets: List<Set>,
        val isCurrent: Boolean = false,
        val isInPreviewMode: Boolean = false,
    ) : Parcelable {

        companion object{
            val MOCK = Exercise(
                id = "cu",
                exerciseRef = "",
                name = "Squat",
                sets = listOf(Set.MOCK, Set.MOCK),
                isCurrent = true,
                isInPreviewMode = true,
            )
        }
        @Parcelize
        data class Set(
            val id:String,
            val type: String,
            val reps: String,
            val restSeconds: Int,
            val weight: String,
            val unit: String,
            val isComplete: Boolean = false
        ) : Parcelable{
            companion object{
                val MOCK = Set(
                    id = "123",
                    type = "warmup",
                    reps = "8-10",
                    restSeconds = 90,
                    weight = "5",
                    unit = "kg",
                    isComplete = false
                )
            }
        }

        fun isComplete() = sets.any() && sets.all { it.isComplete }
        override fun toString(): String {
            // Counting the number of warmup sets
            val warmupSetsCount = sets.count { it.type == "warmup" }

            // Filtering and grouping working sets by their repetition details
            val workingSets =
                sets.filter { it.type == "working" }.groupBy { Pair(it.reps, it.weight) }

            // Building a formatted string for working sets
            val workingSetsSummary = workingSets.entries.joinToString(" | ") { entry ->
                val (reps, weight) = entry.key
                val count = entry.value.size
                "$count x $reps at $weight"
            }

            // Finding a common rest period if applicable, assumes all working sets share the same rest period
            val commonRest = sets.filter { it.type == "working" }.map { it.restSeconds }.distinct()
            val restDisplay =
                if (commonRest.size == 1) "${commonRest.first()} sec rest" else "varied rest"

            // Constructing the final string
            return "$name - $warmupSetsCount warmup | $workingSetsSummary | $restDisplay"
        }
    }

    companion object {
        val EMPTY = WorkoutDm(id = "", title = "", date = Date(), notes = "", isFinished = false)
        val MOCK = WorkoutDm(
            id = "",
            title = "Push and Glutes",
            notes = "Agilli ol!",
            date = Date(),
            isFinished = false,
            exercises = listOf(Exercise(
                id = "mnesarchum",
                exerciseRef = "",
                name = "Lance Willis",
                sets = listOf(),
                isCurrent = false,
                isInPreviewMode = false
            ))
        )
    }
}
