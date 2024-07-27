package az.kodcraft.dashboard.data.di

import az.kodcraft.core.domain.UserManager
import az.kodcraft.dashboard.data.repository.DashboardRepositoryImpl
import az.kodcraft.dashboard.data.repository.TrainerDashboardRepositoryImpl
import az.kodcraft.dashboard.data.service.DashboardService
import az.kodcraft.dashboard.domain.repository.DashboardRepository
import az.kodcraft.dashboard.domain.repository.TrainerDashboardRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

const val ASSIGNED_WORKOUTS_COLLECTION = "assigned_workouts_collection"
const val USERS_COLLECTION = "users_collection"
const val DATE = "date"
const val DASHBOARD_PAGE_SIZE = 5

@Module
@InstallIn(SingletonComponent::class)
object DashboardDataModule {

    @AssignedWorkoutCollection
    @Provides
    fun provideDashboardDataRef() = Firebase.firestore.collection(ASSIGNED_WORKOUTS_COLLECTION)

    @UsersCollection
    @Provides
    fun provideUsersDataRef() = Firebase.firestore.collection(USERS_COLLECTION)

    @Provides
    fun provideDashboardService(
        @AssignedWorkoutCollection assignedWorkoutDataRef: CollectionReference,
        @UsersCollection usersDataRef: CollectionReference
    ): DashboardService = DashboardService(assignedWorkoutDataRef, usersDataRef)

    @Provides
    fun provideDashboardRepository(
        dashboardService: DashboardService
    ): DashboardRepository = DashboardRepositoryImpl(dashboardService)

    @Provides
    fun provideTrainerDashboardRepository(
        @WorkoutsAsc queryWorkoutsByDate: Query,
        @WorkoutsDesc queryWorkoutsByDateDesc: Query,
        @UsersCollection usersCollection: CollectionReference
    ): TrainerDashboardRepository =
        TrainerDashboardRepositoryImpl(queryWorkoutsByDate, queryWorkoutsByDateDesc, usersCollection)

    @WorkoutsAsc
    @Provides
    fun provideDashboardWorkoutsByDate() =  Firebase.firestore.collection(ASSIGNED_WORKOUTS_COLLECTION)
            .where(Filter.equalTo("trainerId", UserManager.getUserId()))
            .orderBy(DATE, Query.Direction.ASCENDING).limit(DASHBOARD_PAGE_SIZE.toLong())

    @WorkoutsDesc
    @Provides
    fun provideDashboardWorkoutsByDateDesc() =  Firebase.firestore.collection(ASSIGNED_WORKOUTS_COLLECTION)
            .where(Filter.equalTo("trainerId", UserManager.getUserId()))
            .orderBy(DATE, Query.Direction.DESCENDING).limit(DASHBOARD_PAGE_SIZE.toLong())

}


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AssignedWorkoutCollection

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UsersCollection

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WorkoutsAsc

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WorkoutsDesc