package com.example.myapplication.ui.mychats

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.R
import com.example.myapplication.databinding.SearchUserDialogBinding
import com.example.myapplication.di.ActivityModule
import com.example.myapplication.di.DaggerMainComponent
import javax.inject.Inject


class SearchUserDialogFragment : DialogFragment(){

    @Inject
    lateinit var vmFactory: ViewModelProvider.Factory
    private val viewModel: MyChatsViewModel by lazy {
        ViewModelProvider(requireParentFragment(), vmFactory).get(MyChatsViewModel::class.java)
    }

    private lateinit var binding: SearchUserDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerMainComponent
            .builder()
            .activityModule(ActivityModule(requireActivity() as AppCompatActivity))
            .build()
            .inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        // Get the layout inflater
        val inflater = activity!!.layoutInflater

        binding = DataBindingUtil.inflate(inflater, R.layout.search_user_dialog, null, false)
        binding.viewModel = viewModel

        binding.lifecycleOwner = this
        viewModel.bind(this)
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(binding.root)
            // Add action buttons
            .setPositiveButton("Search"
            ) { _, _ ->
                viewModel.onCreateChat()
            }
            .setNegativeButton("Close"
            ) { _, _ ->
                this@SearchUserDialogFragment.dialog?.cancel()
            }

        return builder.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.search_user_dialog, container,false)

        binding.viewModel = viewModel

        binding.lifecycleOwner = this
        viewModel.bind(this)

        return binding.root
    }
}