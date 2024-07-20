package az.kodcraft.client.data.di

import az.kodcraft.client.data.repository.ClientRepositoryImpl
import az.kodcraft.client.data.repository.WorkoutRepositoryImpl
import az.kodcraft.client.data.service.ClientService
import az.kodcraft.client.data.service.WorkoutService
import az.kodcraft.client.domain.repository.ClientRepository
import az.kodcraft.client.domain.repository.WorkoutRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier


const val USERS_COLLECTION = "users_collection"
const val trainer_subscriptions_collection = "trainer_subscriptions_collection"
const val workouts_collection = "workouts_collection"
const val assigned_workouts_collection = "assigned_workouts_collection"
@Module
@InstallIn(SingletonComponent::class)
object ClientDataModule {

    @ClientsCollection
    @Provides
    fun provideClientDataRef() = Firebase.firestore.collection(trainer_subscriptions_collection)

    @UsersCollection
    @Provides
    fun provideUserDataRef() = Firebase.firestore.collection(USERS_COLLECTION)

    @WorkoutsCollection
    @Provides
    fun provideWorkoutsDataRef() = Firebase.firestore.collection(workouts_collection)

    @AssignedWorkoutsCollection
    @Provides
    fun provideAssignedWorkoutsDataRef() = Firebase.firestore.collection(assigned_workouts_collection)

    @Provides
    fun provideClientService(
        @UsersCollection usersDataRef: CollectionReference,
        @ClientsCollection clientsDataRef: CollectionReference,
        @AssignedWorkoutsCollection assignedWorkoutsRef: CollectionReference,
        @WorkoutsCollection workoutsRef: CollectionReference,
    ): ClientService = ClientService(usersRef = usersDataRef, clientsRef = clientsDataRef, assignedWorkoutsRef = assignedWorkoutsRef, workoutsRef = workoutsRef )

    @Provides
    fun provideWorkoutService(
        @AssignedWorkoutsCollection assignedWorkoutsRef: CollectionReference,
        @WorkoutsCollection workoutsRef: CollectionReference,
    ): WorkoutService = WorkoutService( assignedWorkoutRef = assignedWorkoutsRef, workoutRef = workoutsRef )

    @Provides
    fun provideClientRepository(
        clientService: ClientService
    ): ClientRepository = ClientRepositoryImpl(clientService)

    @Provides
    fun provideWorkoutRepository(
        workoutService: WorkoutService
    ): WorkoutRepository = WorkoutRepositoryImpl(workoutService)

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WorkoutsCollection
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AssignedWorkoutsCollection
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UsersCollection
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ClientsCollection

