package id.dtprsty.movieme.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commitNow
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.dtprsty.movieme.R
import id.dtprsty.movieme.ui.movie.MovieFragment
import id.dtprsty.movieme.ui.tv_show.TvShowFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_movie.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBottomNavigation()
    }

    private fun initBottomNavigation() {
        openFragment(MovieFragment.newInstance(), "movie")
        navigation.setOnNavigationItemSelectedListener(object :
            BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(@NonNull item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.menu_movie -> {
                        openFragment(MovieFragment.newInstance(), "movie")
                        return true
                    }
                    R.id.menu_tvshow -> {
                        openFragment(TvShowFragment.newInstance(), "tv_show")
                        return true
                    }
                    R.id.menu_favorite -> {
                        return true
                    }
                }
                return false
            }
        })
    }

    private fun openFragment(fragment: Fragment, tag: String) =
        supportFragmentManager.commitNow(allowStateLoss = true) {
            replace(R.id.fl_container, fragment, tag)
        }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        rvMovie.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    animateNavigation(true, navigation)
                } else {
                    animateNavigation(false, navigation)
                }
            }
        })
    }

    var isNavigationHide = false
    fun animateNavigation(hide: Boolean, navigation: BottomNavigationView) {
        if (isNavigationHide && hide || !isNavigationHide && !hide) return
        isNavigationHide = hide
        val moveY = if (hide) 2 * navigation.height else 0
        navigation.animate().translationY(moveY.toFloat()).setStartDelay(100).setDuration(300)
            .start()
    }
}
