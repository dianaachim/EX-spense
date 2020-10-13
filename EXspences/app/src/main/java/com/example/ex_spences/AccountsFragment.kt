package com.example.ex_spences

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ex_spences.Model.Account
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.add_account_card.view.*
import kotlinx.android.synthetic.main.fragment_accounts.*

class AccountsFragment : Fragment() {
    lateinit var mFirebaseAuth: FirebaseAuth
    private var mAccounts = ArrayList<Account>()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_accounts, container, false)
    }

    private fun showMessage(message:String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = Firebase.firestore
        mFirebaseAuth = FirebaseAuth.getInstance()
        val uid = mFirebaseAuth.currentUser?.uid

        db.collection("Accounts")
            .whereEqualTo("uid", uid)
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w("AccountsFragment", "Listen failed.", e)
                    return@addSnapshotListener
                }

                mAccounts.clear()

                for (doc in value!!) {
                    val account = doc.toObject(Account::class.java)
                    mAccounts.add(account)

                }
                accounts_list.apply {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = AccountsAdapter(context, mAccounts)
                    addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                }
                Log.d("AccountsFragment", "Accounts added")
            }





        add_account_button.setOnClickListener {
            val mDialogView = LayoutInflater.from(activity).inflate(R.layout.add_account_card, null)

            AlertDialog.Builder(activity)
                .setView(mDialogView)
                .setTitle("Add account")
                .setPositiveButton("Create", DialogInterface.OnClickListener { _, _ ->
                    val newName = mDialogView.add_account_name_text.text.toString()
                    val newType = mDialogView.add_account_type_text.text.toString()
                    val newBalance = mDialogView.add_account_balance_text.text.toString().toDouble()


                    if (newName == "") {
                        Toast.makeText(activity, "Add account name!", Toast.LENGTH_SHORT).show()
                    } else {
                        val id = db.collection("Accounts").document().id


                        val newAccount: Account = Account(id, newName, newType, newBalance, uid)

                        db.collection("Accounts")
                            .document(id)
                            .set(newAccount)
                            .addOnSuccessListener { documentReference ->
                                Log.w("AccountsFragment", "account added successfully")
                            }
                            .addOnFailureListener { e ->
                                Log.w("AccountsFragment", "error adding account")
                            }
                        mAccounts.add(newAccount)
                        accounts_list.apply {
                            layoutManager = LinearLayoutManager(activity)
                            adapter = AccountsAdapter(context, mAccounts)
                            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                        }
                        accounts_list.adapter?.notifyItemInserted(mAccounts.size+1)
                        accounts_list.adapter?.notifyDataSetChanged()

                    }
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener{ _, _-> })
                .create()
                .show()
        }
    }
}