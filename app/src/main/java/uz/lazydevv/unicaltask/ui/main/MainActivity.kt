package uz.lazydevv.unicaltask.ui.main

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.lazydevv.unicaltask.R
import uz.lazydevv.unicaltask.databinding.ActivityMainBinding
import uz.lazydevv.unicaltask.ui.main.adapters.VpAdapter
import uz.lazydevv.unicaltask.utils.smoothScrollTo

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel by viewModels<MainViewModel>()

    private val binding by viewBinding(ActivityMainBinding::bind)

    private val vpAdapter by lazy(mode = LazyThreadSafetyMode.NONE) {
        VpAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.setupApps()

        setupVp()
    }

    fun swipeNext() {
        with(binding) {
            viewPager.smoothScrollTo(viewPager.currentItem + 1)
        }
    }

    fun swipePrevious() {
        with(binding) {
            viewPager.smoothScrollTo(viewPager.currentItem - 1)
        }
    }

    private fun setupVp() {
        with(binding) {
            viewPager.adapter = vpAdapter

            lifecycleScope.launch(Dispatchers.Main) {
                viewPager.visibility = View.INVISIBLE

                viewPager.setCurrentItem(1, false)
                delay(10)
                viewPager.setCurrentItem(0, false)

                viewPager.visibility = View.VISIBLE
            }
        }
    }
}