package az.kodcraft.workout.data.di

import az.kodcraft.workout.data.repository.AssignedWorkoutRepositoryImpl
import az.kodcraft.workout.data.service.AssignedWorkoutService
import az.kodcraft.workout.domain.repository.AssignedWorkoutRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

const val WORKOUTS_COLLECTION = "workouts_collection"
const val ASSIGNED_WORKOUTS_COLLECTION = "assigned_workouts_collection"
const val EXERCISE_LOGS_COLLECTION = "exercise_logs_collection"
const val EXERCISE_COLLECTION = "exercise_collection"

@Module
@InstallIn(SingletonComponent::class)
object WorkoutDataModule {

    @WorkoutCollection
    @Provides
    fun provideWorkoutDataRef() = Firebase.firestore.collection(WORKOUTS_COLLECTION)

    @AssignedWorkoutCollection
    @Provides
    fun provideAssignedWorkoutDataRef() =
        Firebase.firestore.collection(ASSIGNED_WORKOUTS_COLLECTION)

    @ExerciseLogCollection
    @Provides
    fun provideExerciseLogDataRef() = Firebase.firestore.collection(EXERCISE_LOGS_COLLECTION)


    @ExerciseCollection
    @Provides
    fun provideExerciseDataRef() = Firebase.firestore.collection(EXERCISE_COLLECTION)


    @Provides
    fun provideAssignedWorkoutService(
        @AssignedWorkoutCollection workoutDataRef: CollectionReference,
        @ExerciseLogCollection exerciseLogRef: CollectionReference,
        @ExerciseCollection exerciseRef: CollectionReference,
    ): AssignedWorkoutService = AssignedWorkoutService(workoutDataRef,exerciseRef, exerciseLogRef)

    @Provides
    fun provideWorkoutRepository(
        workoutService: AssignedWorkoutService
    ): AssignedWorkoutRepository = AssignedWorkoutRepositoryImpl(workoutService)
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WorkoutCollection

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AssignedWorkoutCollection

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ExerciseLogCollection

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ExerciseCollection