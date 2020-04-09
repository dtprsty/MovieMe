package id.dtprsty.movieme.ui.main

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commitNow
import com.google.android.material.bottomnavigation.BottomNavigationView
import id.dtprsty.movieme.R
import id.dtprsty.movieme.ui.favorite.FavoriteFragment
import id.dtprsty.movieme.ui.movie.MovieFragment
import id.dtprsty.movieme.ui.tv_show.TvShowFragment
import kotlinx.android.synthetic.main.activity_main.*

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
                        openFragment(FavoriteFragment.newInstance(), "favorite")
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
}
