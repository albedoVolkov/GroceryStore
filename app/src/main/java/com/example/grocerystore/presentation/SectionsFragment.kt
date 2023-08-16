package com.example.grocerystore.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocerystore.databinding.SectionsFragmentBinding
import com.example.grocerystore.domain.API.SectionsAPI
import com.example.grocerystore.domain.adapters.SectionsAdapter
import com.example.grocerystore.domain.helpers.Section
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SectionsFragment : Fragment(), SectionsAdapter.SectionsClickListener {

    private var _binding: SectionsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = SectionsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sectionsAdapter = SectionsAdapter(this)
        binding.recyclerViewSectionsFragment.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewSectionsFragment.adapter = sectionsAdapter


        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io").client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()

        val sectionsApi = retrofit.create(SectionsAPI::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            //error
            val categories = sectionsApi.getSections()
            activity?.runOnUiThread{
                val list : List<Section> = categories.categories
                sectionsAdapter.setList(list)
                Log.d("package:mine",list.toString())
            }
        }

    }

    override fun onItemClick(id: Int, itemView: View) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()!!
        transaction.replace(com.example.grocerystore.R.id.frame_layout_sections_fragment, SectionFragment())
        transaction.addToBackStack(null)
        transaction.commit()
    }

    companion object {
        @JvmStatic
        fun newInstance(): Fragment {
            return SectionsFragment()
        }
    }

}
