import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        findViewById<Button>(R.id.refreshButton).setOnClickListener {
            fetchBusData()
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-33.852, 151.211), 10f))
    }

    private fun fetchBusData() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.example.com/") // Reemplaza con la URL de tu API
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(BusService::class.java)
        service.getBusInfo().enqueue(object : Callback<List<Bus>> {
            override fun onResponse(call: Call<List<Bus>>, response: Response<List<Bus>>) {
                response.body()?.let { buses ->
                    updateMap(buses)
                }
            }

            override fun onFailure(call: Call<List<Bus>>, t: Throwable) {
                // Manejar error
            }
        })
    }

    private fun updateMap(buses: List<Bus>) {
        googleMap.clear()
        buses.forEach { bus ->
            val position = LatLng(bus.latitude, bus.longitude)
            googleMap.addMarker(MarkerOptions().position(position).title(bus.route))
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
