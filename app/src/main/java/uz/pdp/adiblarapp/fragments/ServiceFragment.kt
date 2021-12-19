package uz.pdp.adiblarapp.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import uz.pdp.adiblarapp.R
import uz.pdp.adiblarapp.databinding.FragmentServiceBinding

class ServiceFragment : Fragment(R.layout.fragment_service) {

    private val binding by viewBinding(FragmentServiceBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            cardAbout.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=uz.technocorp.adiblar"))
                startActivity(intent)
            }

            cardShare.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.setType("text/plain")
                intent.putExtra(Intent.EXTRA_SUBJECT, "https://play.google.com/store/apps/details?id=uz.technocorp.adiblar")
                startActivity(Intent.createChooser(intent, "Share using"))
            }

            cardPlus.setOnClickListener {
                findNavController().navigate(R.id.addFragment)
            }

        }
    }
}