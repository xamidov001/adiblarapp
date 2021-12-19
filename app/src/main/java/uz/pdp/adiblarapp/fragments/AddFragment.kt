package uz.pdp.adiblarapp.fragments

import android.Manifest
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import uz.pdp.adiblarapp.BuildConfig
import uz.pdp.adiblarapp.R
import uz.pdp.adiblarapp.room.entity.Writers
import uz.pdp.adiblarapp.databinding.AskDialogBinding
import uz.pdp.adiblarapp.databinding.FragmentAddBinding
import java.io.File
import java.io.IOException

class AddFragment : Fragment(R.layout.fragment_add) {

    private val binding by viewBinding(FragmentAddBinding::bind)
    lateinit var imageFile: File
    private lateinit var uri: Uri
    private var image_url: String? = null
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var storageReference: StorageReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("writers")

        firebaseStorage = FirebaseStorage.getInstance()

        binding.apply {
            setImage.setOnClickListener {
                val millisec = System.currentTimeMillis()
                storageReference = firebaseStorage.getReference("$millisec.img")
                val alert = AlertDialog.Builder(requireContext())
                val dialog_alert = AskDialogBinding.inflate(layoutInflater)
                alert.setView(dialog_alert.root)
                val create = alert.create()
                create.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dialog_alert.apply {
                    camera.setOnClickListener {
                        setPermission(Manifest.permission.CAMERA)
                        create.dismiss()
                    }

                    file.setOnClickListener {
                        setPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        create.dismiss()
                    }
                }
                create.show()
            }


            saveBtn.setOnClickListener {
                val name_edt = nameEdt.text.toString()
                val born_edt = bornEdt.text.toString()
                val death_edt = deathEdt.text.toString()
                val about_edt = aboutEdt.text.toString()
                val type_edt = spinner.selectedItem as String
                if (name_edt.isNotEmpty() && born_edt.isNotEmpty() && death_edt.isNotEmpty() && about_edt.isNotEmpty() && type_edt.isNotEmpty() && image_url != null) {
                    val key = reference.push().key
                    val writer = Writers(key, name_edt, image_url, "($born_edt-$death_edt)", type_edt, about_edt)
                    reference.child(key ?: "").setValue(writer)
                        .addOnCompleteListener {
                            Toast.makeText(
                                requireContext(),
                                "Successfully added",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().popBackStack()
                        }
                }

            }

        }
    }

    private val getPhoto = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.image.setImageURI(uri)
            getImageUrl(uri)
        }
    }

    private fun getImageUrl(ur: Uri) {
        storageReference.putFile(ur)
            .addOnSuccessListener {
                if (it?.task?.isSuccessful == true) {
                    val downloadUrl = it.metadata?.reference?.downloadUrl
                    downloadUrl?.addOnSuccessListener { url_img ->
                        image_url = url_img.toString()
                    }
                }
            }
    }

    private var takePhoto = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            binding.image.setImageURI(uri)
            getImageUrl(uri)
        }
    }

    @Throws(IOException::class)
    fun createImageFile(): File {
        val m = System.currentTimeMillis()
        val externalFilesDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile("Image_$m", ".jpg", externalFilesDir)
    }

    fun setPermission(permission: String) {
        Dexter.withContext(requireContext())
            .withPermissions(permission)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            if (permission == Manifest.permission.READ_EXTERNAL_STORAGE) {
                                getPhoto.launch("image/*")
                            } else {
                                imageFile = createImageFile()
                                uri = FileProvider.getUriForFile(
                                    requireContext(),
                                    BuildConfig.APPLICATION_ID,
                                    imageFile
                                )
                                takePhoto.launch(uri)
                            }
                        }

                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    // Remember to invoke this method when the custom rationale is closed
                    // or just by default if you don't want to use any custom rationale.
                    token?.continuePermissionRequest()
                }

            })
            .withErrorListener {
                Toast.makeText(requireContext(), "${it.name}", Toast.LENGTH_SHORT).show()
            }
            .check()
    }
}