package com.amercosovic.mycarlistfirebasedemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_my_car_list.*

class MyCarList : AppCompatActivity() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val dbFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_car_list)

        val currentUsersEmail = mAuth.currentUser?.email.toString()

        fetchCarList(currentUsersEmail)

        btnBackToHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
//            intent.putExtra("email",currentUsersEmail)
            startActivity(intent)
        }


    }

    private fun fetchCarList(currentUserEmail: String) {
        val userRef = dbFirestore.collection("CarListUsers").document(currentUserEmail).collection("MyCars")
//        Toast.makeText(this, currentUserEmail, Toast.LENGTH_LONG).show()

        userRef.get().addOnCompleteListener {
            val result: StringBuffer = StringBuffer()

            if (it.isSuccessful) {
//                Toast.makeText(this, "Displaying your cars", Toast.LENGTH_LONG).show()
                for (document in it.result!!) {
                    result.append(document.data.getValue("year")).append(" ")
                    result.append(document.data.getValue("make")).append(" ")
                    result.append(document.data.getValue("model")).append("\n\n")
                }
                txtAllUsers.setText(result)
            }
        }
//            val result: StringBuffer = StringBuffer()
//                result.append(document.data?.getValue("year")).append(",")
//                result.append(document.data?.getValue("make")).append(",")
//                result.append(document.data?.getValue("model")).append("\n")

    }
}