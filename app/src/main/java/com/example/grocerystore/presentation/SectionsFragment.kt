package com.example.grocerystore.presentation

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Website.URL
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.grocerystore.R
import com.example.grocerystore.databinding.ActivityMainBinding
import com.example.grocerystore.databinding.SectionsFragmentBinding
import com.example.grocerystore.domain.API.SectionsAPI
import com.example.grocerystore.domain.adapters.PagerAdapter
import com.example.grocerystore.domain.adapters.SectionsAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.InputStream
import java.net.URL


class SectionsFragment : Fragment(), SectionsAdapter.SectionsClickListener {

    private var _binding: SectionsFragmentBinding? = null
    private val binding get() = _binding!!
    private var _sectionsAdapter : SectionsAdapter? = null
    private val sectionsAdapter get() = _sectionsAdapter!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = SectionsFragmentBinding.inflate(inflater, container, false)
        _sectionsAdapter = SectionsAdapter(this)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//
//        val retrofit = Retrofit.Builder().baseUrl("https://run.mocky.io/v3/").addConverterFactory(
//            GsonConverterFactory.create()).build()
//        val sectionsApi = retrofit.create(SectionsAPI::class.java)
//        //do getting info in other thread
//        CoroutineScope(Dispatchers.IO).launch {
//            val sections = sectionsApi.getSections()
//            activity?.runOnUiThread{
//                sectionsAdapter.setList(sections)
//            }
//        }

    }

    override fun onItemClick(id: Long, itemView: View) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()!!
        transaction.replace(R.id.frame_layout_sections_fragment, SectionFragment())
        transaction.disallowAddToBackStack()
        transaction.commit()
    }

    companion object {
        @JvmStatic
        fun newInstance(): Fragment {
            return SectionsFragment()
        }
    }

}
