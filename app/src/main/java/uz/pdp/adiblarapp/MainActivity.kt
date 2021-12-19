package uz.pdp.adiblarapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import uz.pdp.adiblarapp.databinding.ActivityMainBinding
import uz.pdp.adiblarapp.room.database.DbHelper
import uz.pdp.telegramm.preference.MyShared

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private var myShared = MyShared
    private lateinit var dbHelper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("users")
        myShared = MyShared.getInstance(this)

        if (myShared.getList("uid").isEmpty()) {
            val key = reference.push().key

            reference.setValue(key ?: "")
                .addOnCompleteListener {
                    myShared.setList(key, "uid")
                }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.my_support_nav).navigateUp()
    }
}