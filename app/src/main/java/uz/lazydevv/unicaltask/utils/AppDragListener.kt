package uz.lazydevv.unicaltask.utils

import android.view.DragEvent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.lazydevv.unicaltask.R
import uz.lazydevv.unicaltask.models.AppM
import uz.lazydevv.unicaltask.ui.apps.adapters.RvAppsAdapter
import uz.lazydevv.unicaltask.ui.main.MainActivity
import kotlin.math.roundToInt

private var fakeItemAdapter: RvAppsAdapter? = null

class AppDragListener(
    private val fragment: Fragment
) : View.OnDragListener {

    private var shouldAct = true

    private var canDrop = true
        set(value) {
            field = value

            if (!value) {
                fragment.lifecycleScope.launch {
                    delay(250)
                    field = true
                }
            }
        }

    override fun onDrag(view: View, event: DragEvent): Boolean {
        when (event.action) {
            DragEvent.ACTION_DRAG_LOCATION -> {
                controlDragLocationState(view, event)
                controlDropState(view, event, false)
            }

            DragEvent.ACTION_DROP -> {
                controlDropState(view, event, true)
            }
        }

        return true
    }

    private fun controlDragLocationState(view: View, event: DragEvent) {
        val sourceView = event.localState as View

        val windowLoc = IntArray(2)
        view.getLocationInWindow(windowLoc)

        val fixedX = event.x.roundToInt() + windowLoc.first()

        val isPrev = fixedX <= sourceView.width / 2.5
        val isNext = fixedX >= fragment.requireActivity().window.decorView.width - (sourceView.width / 3)

        when {
            shouldAct && isPrev -> {
                shouldAct = false
                (fragment.requireActivity() as? MainActivity?)?.swipePrevious()
            }

            shouldAct && isNext -> {
                shouldAct = false
                (fragment.requireActivity() as? MainActivity?)?.swipeNext()
            }

            !isPrev && !isNext && !shouldAct -> {
                shouldAct = true
            }
        }
    }

    private fun controlDropState(view: View, event: DragEvent, isRealDrop: Boolean) {
        if (isRealDrop) canDrop = true
        if (!canDrop) return

        if (view.id == R.id.ll_app_item || view.id == R.id.rv_apps) {
            val sourceView = event.localState as View

            val fromRv = sourceView.parent as RecyclerView
            val toRv = if (view.id == R.id.ll_app_item) view.parent as RecyclerView else view as RecyclerView

            val fromAdapter = fromRv.adapter as RvAppsAdapter
            val toAdapter = toRv.adapter as RvAppsAdapter

            val fromPos = fromRv.getChildAdapterPosition(sourceView)
            var toPos = if (view.id == R.id.ll_app_item) {
                toRv.getChildAdapterPosition(view)
            } else {
                if (fromRv == toRv) toAdapter.itemCount - 1 else {
                    if (toAdapter.itemCount == 0) 0 else toAdapter.itemCount - 1
                }
            }

            val sourceItem = sourceView.tag as AppM

            if (fromRv == toRv) {
                // in the same group;
                fromAdapter.diffUtil.currentList.toMutableList().apply {
                    removeAt(fromPos)
                    add(toPos, sourceItem)
                    fromAdapter.diffUtil.submitList(this)
                }

                if (isRealDrop) {
                    fakeItemAdapter?.diffUtil?.currentList?.toMutableList()?.let {
                        it.removeAt(it.indexOf(sourceItem))
                        fakeItemAdapter?.diffUtil?.submitList(it)
                        fakeItemAdapter = null
                    }
                }
            } else {
                // in different groups;
                if (fakeItemAdapter == null) fakeItemAdapter = toAdapter

                if (isRealDrop) {
                    fromAdapter.diffUtil.currentList.toMutableList().apply {
                        removeAt(fromPos)
                        fromAdapter.diffUtil.submitList(this)
                    }

                    fakeItemAdapter = null
                } else {
                    val fakeItemPos = toAdapter.diffUtil.currentList.indexOf(sourceItem)

                    toAdapter.diffUtil.currentList.toMutableList().apply {
                        if (fakeItemPos != -1) removeAt(fakeItemPos) else toPos++
                        add(toPos, sourceItem)
                        toAdapter.diffUtil.submitList(this)
                    }
                }
            }

            canDrop = false
        }
    }
}