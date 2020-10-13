package com.example.ex_spences

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ex_spences.Model.Account
import com.example.ex_spences.Model.PiggyBank
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_accounts.*


class VaultFragment : Fragment() {
    lateinit var mFirebaseAuth: FirebaseAuth
    private var mSavings = ArrayList<PiggyBank>()
    private var mPiggyBankId = ""

    private fun showMessage(message:String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    fun getUserSavings(callback: SavingsListCallback) { // ii dai ca parametru interfata cu callback-ul
        val user = FirebaseAuth.getInstance().uid
        val db = Firebase.firestore
        if (user != null) {
            db.collection("PiggyBank")
                .whereEqualTo("uid", user)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val savings = ArrayList<PiggyBank>()
                        for (document in task.result!!) {
                            val pgid = document.get("pgid") as String
                            val value = document.get("value") as Double
                            val uid = document.get("uid") as String
                            val piggyBank = PiggyBank(pgid, value, uid)
                            savings.add(piggyBank)
                        }
                        callback.onCallback(savings) // callback la metoda din interfata dupa ce se termina de luat toate entitatile
                    } else {
                        Toast.makeText(context, task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vault, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var savingsEditText = view.findViewById<TextView>(R.id.vault_text)

        val db = Firebase.firestore
        mFirebaseAuth = FirebaseAuth.getInstance()
        val uid = mFirebaseAuth.currentUser?.uid

        db.collection("PiggyBank")
            .whereEqualTo("uid", uid)
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w("VaultFragment", "Listen failed.", e)
                    return@addSnapshotListener
                }

                mSavings.clear()

                for (doc in value!!) {
                    val vault = doc.toObject(PiggyBank::class.java)
                    mSavings.add(vault)
                }
                mPiggyBankId = mSavings.get(0).pgid
                savingsEditText?.text = mSavings.get(0).value.toString()

                Log.d("VaultFragment", "Vault added")
            }

        getUserSavings(object : SavingsListCallback{
            override fun onCallback(value: List<PiggyBank>) {
                mSavings = value as ArrayList<PiggyBank>
                mPiggyBankId = mSavings.get(0).pgid
                savingsEditText?.text = mSavings.get(0).value.toString()
            }
        })
    }


}