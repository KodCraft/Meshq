package az.kodcraft.dashboard.data.di

import az.kodcraft.dashboard.data.repository.DashboardRepositoryImpl
import az.kodcraft.dashboard.data.service.DashboardService
import az.kodcraft.dashboard.domain.repository.DashboardRepository
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

const val ASSIGNED_WORKOUTS_COLLECTION = "assigned_workouts_collection"
@Module
@InstallIn(SingletonComponent::class)
object DashboardDataModule {

    @AssignedWorkoutCollection
    @Provides
    fun provideDashboardDataRef() = Firebase.firestore.collection(ASSIGNED_WORKOUTS_COLLECTION)

    @Provides
    fun provideDashboardService(
        @AssignedWorkoutCollection assignedWorkoutDataRef: CollectionReference
    ): DashboardService = DashboardService(assignedWorkoutDataRef)

    @Provides
    fun provideDashboardRepository(
        dashboardService: DashboardService
    ): DashboardRepository = DashboardRepositoryImpl(dashboardService)
}
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AssignedWorkoutCollection