package nel.marco.multidependency

import dagger.Component
import dagger.Module
import dagger.Provides
import jakarta.inject.Singleton


@Module
class VehicleModule {

    @Singleton
    @Provides
    fun brand() = Brand()

    @Singleton
    @Provides
    fun engine() = Engine()
}

@Singleton
@Component(modules = [VehicleModule::class])
interface VehiclesComponent {
    fun buildCar(): Car
}