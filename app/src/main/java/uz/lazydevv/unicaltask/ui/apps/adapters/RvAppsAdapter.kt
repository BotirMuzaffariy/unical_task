package uz.lazydevv.unicaltask.ui.apps.adapters

import android.content.ClipData
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnDragListener
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uz.lazydevv.unicaltask.databinding.ItemRvAppBinding
import uz.lazydevv.unicaltask.models.AppM

class RvAppsAdapter(
    private val onDragListener: OnDragListener
) : RecyclerView.Adapter<RvAppsAdapter.AppVh>() {

    private val diffUtilCallback = object : DiffUtil.ItemCallback<AppM>() {
        override fun areItemsTheSame(oldItem: AppM, newItem: AppM): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AppM, newItem: AppM): Boolean {
            return oldItem.name == newItem.name
        }
    }

    val diffUtil = AsyncListDiffer(this, diffUtilCallback)

    inner class AppVh(private val itemBinding: ItemRvAppBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(appM: AppM) {
            with(itemBinding) {
                ivIcon.setImageResource(appM.icon)
                tvName.text = appM.name
                root.tag = appM

                itemView.setOnDragListener(onDragListener)

                itemView.setOnLongClickListener {
                    val data = ClipData.newPlainText("", "")
                    val shadowBuilder = View.DragShadowBuilder(root)

                    root.startDragAndDrop(data, shadowBuilder, root, 0)

                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppVh {
        return AppVh(ItemRvAppBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = diffUtil.currentList.size

    override fun onBindViewHolder(holder: AppVh, position: Int) {
        holder.onBind(diffUtil.currentList[position])
    }
}