import retrofit2.Call
import retrofit2.http.GET

interface BusService {
    @GET("bus-info")
    fun getBusInfo(): Call<List<Bus>>
}
