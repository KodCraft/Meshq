package az.kodcraft.client.data.di

import az.kodcraft.client.data.repository.ClientRepositoryImpl
import az.kodcraft.client.data.service.ClientService
import az.kodcraft.client.domain.repository.ClientRepository
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
@Module
@InstallIn(SingletonComponent::class)
object ClientDataModule {

    @ClientsCollection
    @Provides
    fun provideClientDataRef() = Firebase.firestore.collection(trainer_subscriptions_collection)
    @UsersCollection
    @Provides
    fun provideUserDataRef() = Firebase.firestore.collection(USERS_COLLECTION)

    @Provides
    fun provideClientService(
        @UsersCollection usersDataRef: CollectionReference,
        @ClientsCollection clientsDataRef: CollectionReference,
    ): ClientService = ClientService(usersDataRef, clientsDataRef)

    @Provides
    fun provideClientRepository(
        clientService: ClientService
    ): ClientRepository = ClientRepositoryImpl(clientService)

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UsersCollection
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ClientsCollection

