package az.kodcraft.trainer.data.di

import az.kodcraft.trainer.data.repository.TrainerRepositoryImpl
import az.kodcraft.trainer.data.service.TrainerService
import az.kodcraft.trainer.domain.repository.TrainerRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier


const val USERS_COLLECTION = "users_collection"
const val USER_STATS_COLLECTION = "users_stats_collection"
const val subscription_requests_collection = "subscription_requests_collection"
const val notifications_collection = "notifications_collection"

@Module
@InstallIn(SingletonComponent::class)
object ClientDataModule {

    @UsersCollection
    @Provides
    fun provideUserDataRef() = Firebase.firestore.collection(USERS_COLLECTION)

    @UserStatsCollection
    @Provides
    fun provideUserStatsDataRef() = Firebase.firestore.collection(USER_STATS_COLLECTION)

    @SubscriptionRequestsCollection
    @Provides
    fun provideSubscriptionRequestsDataRef() =
        Firebase.firestore.collection(subscription_requests_collection)

    @NotificationsCollection
    @Provides
    fun provideNotificationRequestsDataRef() =
        Firebase.firestore.collection(notifications_collection)

    @Provides
    fun provideTrainerService(
        @UsersCollection usersDataRef: CollectionReference,
        @UserStatsCollection userStatsRef: CollectionReference,
        @SubscriptionRequestsCollection subscriptionRequestsRef: CollectionReference,
        @NotificationsCollection notifiactionsRequestsRef: CollectionReference,
    ): TrainerService = TrainerService(
        usersDataRef,
        userStatsRef,
        subscriptionRequestsRef,
        notifiactionsRequestsRef
    )

    @Provides
    fun provideTrainerRepository(
        trainerService: TrainerService
    ): TrainerRepository = TrainerRepositoryImpl(trainerService)

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UsersCollection

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserStatsCollection

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SubscriptionRequestsCollection

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NotificationsCollection

