package az.kodcraft.workout.data.di

import az.kodcraft.workout.data.repository.WorkoutRepositoryImpl
import az.kodcraft.workout.data.service.WorkoutService
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
@Module
@InstallIn(SingletonComponent::class)
object WorkoutDataModule {

    @WorkoutCollection
    @Provides
    fun provideWorkoutDataRef() = Firebase.firestore.collection(WORKOUTS_COLLECTION)

    @Provides
    fun provideWorkoutService(
        @WorkoutCollection workoutDataRef: CollectionReference
    ): WorkoutService = WorkoutService(workoutDataRef)

    @Provides
    fun provideWorkoutRepository(
         workoutService: WorkoutService
    ): WorkoutRepository = WorkoutRepositoryImpl(workoutService)
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WorkoutCollection