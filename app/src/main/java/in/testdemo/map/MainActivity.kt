package `in`.testdemo.map

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        splash_view.post {
            val navHost = nav_cal_fragment as NavHostFragment
            val graph = navHost.navController
                    .navInflater.inflate(R.navigation.navigation_ekar)
            graph.startDestination = R.id.mapFragment
            navHost.navController.graph = graph
            navController = Navigation.findNavController(this, R.id.nav_cal_fragment)
        }
        splash_view.postDelayed({ splash_view.visibility = View.GONE }, 1000)
    }

    override fun onSupportNavigateUp():Boolean {
        return if (navController == null) {
            true
        } else {
            navController?.navigateUp()!!
        }
    }
}