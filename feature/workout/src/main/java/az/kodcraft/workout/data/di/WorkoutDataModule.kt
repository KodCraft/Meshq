package az.kodcraft.workout.data.di

import az.kodcraft.workout.data.repository.AssignedWorkoutRepositoryImpl
import az.kodcraft.workout.data.repository.ExerciseRepositoryImpl
import az.kodcraft.workout.data.repository.WorkoutRepositoryImpl
import az.kodcraft.workout.data.service.AssignedWorkoutService
import az.kodcraft.workout.data.service.ExerciseService
import az.kodcraft.workout.data.service.WorkoutService
import az.kodcraft.workout.domain.repository.AssignedWorkoutRepository
import az.kodcraft.workout.domain.repository.ExerciseRepository
import az.kodcraft.workout.domain.repository.WorkoutRepository
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
const val EXERCISE_COLLECTION = "exercises_collection"

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
    ): AssignedWorkoutService = AssignedWorkoutService(workoutDataRef, exerciseLogRef)

    @Provides
    fun provideWorkoutService(
        @WorkoutCollection workoutDataRef: CollectionReference,
        @ExerciseCollection exerciseRef: CollectionReference,
    ): WorkoutService = WorkoutService(workoutDataRef, exerciseRef)

    @Provides
    fun provideWorkoutRepository(
        workoutService: WorkoutService
    ): WorkoutRepository = WorkoutRepositoryImpl(workoutService)

    @Provides
    fun provideAssignedWorkoutRepository(
        workoutService: AssignedWorkoutService
    ): AssignedWorkoutRepository = AssignedWorkoutRepositoryImpl(workoutService)

    @Provides
    fun provideExerciseService(
        @ExerciseCollection exerciseRef: CollectionReference,
    ): ExerciseService = ExerciseService(exerciseRef)

    @Provides
    fun provideExerciseRepository(
        exerciseService: ExerciseService
    ): ExerciseRepository = ExerciseRepositoryImpl(exerciseService)
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