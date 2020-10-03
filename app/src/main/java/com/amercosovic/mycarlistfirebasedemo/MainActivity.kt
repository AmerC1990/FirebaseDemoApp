package com.amercosovic.mycarlistfirebasedemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_my_car_list.*
import java.time.Year

class MainActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val dbFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (mAuth.currentUser?.email != null) {
            Toast.makeText(this, mAuth?.currentUser?.email.toString(), Toast.LENGTH_LONG).show()
            editEmail.visibility = View.INVISIBLE
            editPassword.visibility  = View.INVISIBLE
            editCarYear.visibility = View.VISIBLE
            editCarMake.visibility = View.VISIBLE
            editCarModel.visibility = View.VISIBLE
            btnSubmitCar.visibility = View.VISIBLE
            btnViewAllCarsList.visibility = View.VISIBLE
        }

        btnLogin.setOnClickListener {
            val email: String = editEmail.text.toString()
            val password: String = editPassword.text.toString()

            login(email, password)
        }

        btnLogout.setOnClickListener {
            logOut()
        }

        btnRegister.setOnClickListener {
            val email: String = editEmail.text.toString()
            val password: String = editPassword.text.toString()

            registerUser(email, password)
        }

        btnSubmitCar.setOnClickListener {
            addCarInfo(email = mAuth?.currentUser?.email.toString(),
                carYear = editCarYear.text.toString(),
                carMake = editCarMake.text.toString(),
                carModel = editCarModel.text.toString())
        }

        btnViewAllCarsList.setOnClickListener {
            val intent = Intent(this, MyCarList::class.java)
            startActivity(intent)
        }

    }

    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            if (it.isSuccessful) {
                    Toast.makeText(this, "User Login success", Toast.LENGTH_LONG).show()
                    val ref = dbFirestore.collection("CarListUsers").document(email)
                    ref.get().addOnSuccessListener {document -> if (document != null) {
                    editCarYear.visibility = View.VISIBLE
                    editCarMake.visibility = View.VISIBLE
                    editCarModel.visibility = View.VISIBLE
                    btnViewAllCarsList.visibility = View.VISIBLE
                    btnSubmitCar.visibility = View.VISIBLE
                    editEmail.visibility = View.INVISIBLE
                    editPassword.visibility  = View.INVISIBLE
                }
            }
        }
    }
}


    private fun logOut() {
        mAuth.signOut()
        editCarYear.visibility = View.INVISIBLE
        editCarMake.visibility = View.INVISIBLE
        editCarModel.visibility = View.INVISIBLE
        btnSubmitCar.visibility = View.INVISIBLE
        btnViewAllCarsList.visibility  = View.INVISIBLE
        editEmail.visibility = View.VISIBLE
        editPassword.visibility  = View.VISIBLE
        editEmail.text.clear()
        editPassword.text.clear()
    }

    private fun registerUser(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            when {
                it.isSuccessful -> {
                    Toast.makeText(this, "User Registration success", Toast.LENGTH_LONG).show()
                }
                else -> {
                    Toast.makeText(this, "User Registration failed", Toast.LENGTH_LONG).show()
                }
            }

        }
    }

    private fun addCarInfo(email: String,carYear: String, carMake: String, carModel: String) {
        val car = hashMapOf(
            "make" to carMake,
            "model" to carModel,
            "year" to carYear
        )

        val ref2 = dbFirestore.collection("CarListUsers").document(email).collection("MyCars").document(carMake)
        ref2.set(car).addOnCompleteListener {
            when {
                it.isSuccessful -> {
                    Toast.makeText(this, "New Car Added", Toast.LENGTH_LONG).show()
                    btnViewAllCarsList.visibility = View.VISIBLE
                }
                else -> {
                    Toast.makeText(this, "New Car Failed to add", Toast.LENGTH_LONG).show()
                }
            }
        }




    }
}