package az.kodcraft.workout.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.random.Random

@Parcelize
data class CreateWorkoutDm(
    val id: String,
    val title: String,
    val notes: String,
    val exercises: List<Exercise> = emptyList()
) : Parcelable {
    @Parcelize
    data class Exercise(
        val id: String,
        val exerciseRefId: String,
        val name: String,
        val sets: List<Set>,
    ) : Parcelable {

        companion object{
            val MOCK = Exercise(
                id = "123",
                exerciseRefId = "",
                name = "Squat",
                sets = listOf(Set.MOCK, Set.MOCK),
            )
            val EMPTY = Exercise(
                id = "",
                exerciseRefId = "",
                name = "",
                sets = emptyList(),
            )
        }
        @Parcelize
        data class Set(
            val id:String,
            val type: String,
            val reps: String,
            val restSeconds: String,
            val weight: String,
            val unit: String,
        ) : Parcelable{
            companion object{
                val MOCK = Set(
                    id = "123",
                    type = "warmup",
                    reps = "8-10",
                    restSeconds = "90",
                    weight = "5",
                    unit = "kg",
                )

                val EMPTY = Set(
                    id = Random.nextLong().toString(),
                    type = "warmup",
                    reps = "0",
                    restSeconds = "0",
                    weight = "0",
                    unit = "kg"
                )
            }
        }

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
        val EMPTY = CreateWorkoutDm(id = "", title = "", notes = "")
        val MOCK = CreateWorkoutDm(
            id = "",
            title = "Push and Glutes",
            notes = "Agilli ol!",
            exercises = listOf(Exercise(
                id = "123",
                exerciseRefId = "",
                name = "Glute Bridge",
                sets = listOf(),
            ))
        )
    }
}
