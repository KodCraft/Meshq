package az.kodcraft.workout.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkoutDm(
    val id: String,
    val title: String,
    val date: String,
    val notes: String,
    val exercises: List<Exercise> = emptyList()
) : Parcelable {


    fun isComplete() = exercises.any() && exercises.all { it.isComplete() }
    @Parcelize
    data class Exercise(
        val id: String,
        val name: String,
        val sets: List<Set>,
        val isCurrent: Boolean = false,
        val isInPreviewMode: Boolean = false,
    ) : Parcelable {
        @Parcelize
        data class Set(
            val id:String,
            val type: String,
            val reps: String,
            val restSeconds: Int,
            val weight: String,
            val isComplete: Boolean = false
        ) : Parcelable

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
        val EMPTY = WorkoutDm(id = "", title = "", date = "", notes = "")
        val MOCK = WorkoutDm(
            id = "",
            title = "Push and Glutes",
            notes = "Agilli ol!",
            date = "16 Feb"
        )
    }
}
