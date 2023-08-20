package uz.lazydevv.unicaltask.ui.apps

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.lazydevv.unicaltask.R
import uz.lazydevv.unicaltask.databinding.FragmentAppsBinding
import uz.lazydevv.unicaltask.ui.apps.adapters.RvAppsAdapter
import uz.lazydevv.unicaltask.utils.AppDragListener

class AppsFragment : Fragment(R.layout.fragment_apps) {

    private val viewModel by viewModels<AppsViewModel>()

    private val binding by viewBinding(FragmentAppsBinding::bind)

    private val appsAdapter by lazy(mode = LazyThreadSafetyMode.NONE) {
        RvAppsAdapter(dragListener)
    }

    private val dragListener by lazy(mode = LazyThreadSafetyMode.NONE) {
        AppDragListener(fragment = this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.setFragmentPos(arguments?.getInt(KEY_POSITION, 0))
        viewModel.getApps()

        observeApps()

        appsAdapter.diffUtil.addListListener { _, currentList ->
            viewModel.setReorderedApps(currentList)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            rvApps.adapter = appsAdapter
            rvApps.setOnDragListener(dragListener)
            requireActivity().window.decorView.setOnDragListener(dragListener)
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.saveStates()
    }

    private fun observeApps() {
        lifecycleScope.launch {
            viewModel.apps.collectLatest { appsList ->
                appsAdapter.diffUtil.submitList(appsList)
            }
        }
    }

    companion object {

        private const val KEY_POSITION = "position"

        fun newInstance(position: Int): AppsFragment = AppsFragment().apply {
            arguments = bundleOf(KEY_POSITION to position)
        }
    }
}