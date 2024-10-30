package com.mediakom.pushnotif

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mediakom.pushnotif.databinding.ActivityMainBinding
import java.security.MessageDigest
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: FirebaseDatabase
    private lateinit var firebaseAuth: FirebaseAuth
    private var senderId: String = ""
    private var destinationId: String = ""
    private var chatList = mutableListOf<ChatModel>()
    private var destinationRoom: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = Firebase.database
        firebaseAuth = Firebase.auth
        senderId = intent.getStringExtra("senderId").toString()
        destinationId = intent.getStringExtra("destinationId").toString()

        db.getReference("message/room").equalTo("$destinationId")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snapshot in snapshot.children){
                        destinationRoom = destinationId
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    destinationRoom = senderId
                }

            })
        Log.d("ceklog",destinationRoom)
        var listItemChatRefernce = db.getReference("message/room/$destinationRoom")
        listItemChatRefernce.orderByChild("timestamp").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                for (snapshot in snapshot.children){
                    if (snapshot == null){
                        break
                    }
                    chatList.add(snapshot.getValue(ChatModel::class.java)!!)
                    setListChat(chatList)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        binding.btnSendChat.setOnClickListener {
            var timestamp = System.currentTimeMillis()
            var chatReference = db.getReference("message/room/$destinationRoom").child(
                timestamp.toString()
            )
            chatReference.setValue(ChatModel(senderId,timestamp,binding.etChat.text.toString()))
            var userSenderReference = db.getReference("user/$senderId/list_chat/$destinationRoom")
            var userDestinationReference = db.getReference("user/$destinationId/list_chat/$destinationRoom")
            userSenderReference.setValue(ChatRoomModel(destinationRoom,destinationId))
            userDestinationReference.setValue(ChatRoomModel(destinationRoom,senderId))
            binding.etChat.setText("")
        }

    }

    fun hashStringWithSHA256(str: String): String {
        val messageDigest = MessageDigest.getInstance("SHA-1")
        val byteArray = messageDigest.digest(str.toByteArray())
        return byteArray.fold("") { str, it -> str + "%02x".format(it) }
    }

    private fun setListChat(it: List<ChatModel>) {
        val layoutManager = LinearLayoutManager(this)
        binding.rvChat.setLayoutManager(layoutManager)
        binding.rvChat.setHasFixedSize(true)
        val adapter = ListChatAdapter(senderId)
        binding.rvChat.adapter = adapter
        adapter.submitList(it)
        adapter.notifyDataSetChanged()
        binding.rvChat.scrollToPosition(it.size-1)
    }
}