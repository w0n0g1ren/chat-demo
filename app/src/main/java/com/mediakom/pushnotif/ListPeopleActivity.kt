package com.mediakom.pushnotif

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mediakom.pushnotif.databinding.ActivityListPeopleBinding

class ListPeopleActivity : AppCompatActivity(), OnClick {
    private lateinit var binding: ActivityListPeopleBinding
    private var userList = mutableListOf<UserModel>()
    private lateinit var db: FirebaseDatabase
    private var senderId: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPeopleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = Firebase.database
        senderId = intent.getStringExtra("senderId").toString()

        val userReference = db.getReference("user")
        userReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("datasnapshot",snapshot.toString())
                if (userList == null){
                    for (dataSnapshot in snapshot.children){
                        userList.add(dataSnapshot.getValue(UserModel::class.java)!!)
                    }
                } else {
                    userList.clear()
                    for (dataSnapshot in snapshot.children){
                        userList.add(dataSnapshot.getValue(UserModel::class.java)!!)
                    }
                }

                setListPeople(userList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setListPeople(it: List<UserModel>) {
        val layoutManager = LinearLayoutManager(this)
        binding.rvPeople.setLayoutManager(layoutManager)
        binding.rvPeople.setHasFixedSize(true)
        val adapter = ListPeopleAdapter(senderId,this)
        binding.rvPeople.adapter = adapter
        adapter.submitList(it)
    }

    override fun onClick(position: Int) {

    }
}