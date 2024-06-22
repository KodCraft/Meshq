package az.kodcraft.notification.data.di

import az.kodcraft.notification.data.repository.NotificationRepositoryImpl
import az.kodcraft.notification.data.service.NotificationService
import az.kodcraft.notification.domain.repository.NotificationRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier


const val trainer_subscriptions_collection = "trainer_subscriptions_collection"
const val notifications_collection = "notifications_collection"
const val subscription_requests_collection = "subscription_requests_collection"

@Module
@InstallIn(SingletonComponent::class)
object ClientDataModule {

    @TrainerSubsCollection
    @Provides
    fun provideUserDataRef() = Firebase.firestore.collection(trainer_subscriptions_collection)

    @NotificationsCollection
    @Provides
    fun provideUserStatsDataRef() = Firebase.firestore.collection(notifications_collection)

    @SubscriptionRequestsCollection
    @Provides
    fun provideSubscriptionRequestsDataRef() =
        Firebase.firestore.collection(subscription_requests_collection)

    @Provides
    fun provideNotificationService(
        @TrainerSubsCollection trainerSubscriptionsRef: CollectionReference,
        @SubscriptionRequestsCollection subscriptionRequestsRef: CollectionReference,
        @NotificationsCollection notificationsRef: CollectionReference,
    ): NotificationService = NotificationService(
        trainerSubscriptionsRef = trainerSubscriptionsRef,
        subscriptionRequestsRef = subscriptionRequestsRef,
        notificationsRef = notificationsRef
    )

    @Provides
    fun provideNotificationRepository(
        notificationService: NotificationService
    ): NotificationRepository = NotificationRepositoryImpl(notificationService)

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TrainerSubsCollection

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NotificationsCollection

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SubscriptionRequestsCollection

