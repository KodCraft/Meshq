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
@Module
@InstallIn(SingletonComponent::class)
object WorkoutDataModule {

    @WorkoutCollection
    @Provides
    fun provideWorkoutDataRef() = Firebase.firestore.collection(WORKOUTS_COLLECTION)
    @AssignedWorkoutCollection
    @Provides
    fun provideAssignedWorkoutDataRef() = Firebase.firestore.collection(ASSIGNED_WORKOUTS_COLLECTION)


    @Provides
    fun provideAssignedWorkoutService(
        @AssignedWorkoutCollection workoutDataRef: CollectionReference
    ): AssignedWorkoutService = AssignedWorkoutService(workoutDataRef)

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