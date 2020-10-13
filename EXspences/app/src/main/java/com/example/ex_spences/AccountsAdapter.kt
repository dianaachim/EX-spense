package com.example.ex_spences

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ex_spences.Model.Account

class AccountsAdapter(mainContext : Context, private val list: ArrayList<Account>) : RecyclerView.Adapter<AccountsViewHolder>() {
    var mContext = mainContext


//    private var disposable: Disposable? = null

//    private fun isOnline(context: Context): Boolean {
//        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val networkInfo=connectivityManager.activeNetworkInfo
//        return  networkInfo!=null && networkInfo.isConnected
//    }

    private fun showMessage(message: String) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return AccountsViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int = list.size


    override fun onBindViewHolder(holder: AccountsViewHolder, position: Int) {
//        db = mContext?.let { it1 -> BookDb.getBookDb(context = it1) }
//        bookDao = db?.bookDao()

        val account = list[holder.adapterPosition]
        holder.bind(account)

    }
}

class AccountsViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.account_details_card, parent, false)) {
    private var mName: TextView? = null
    private var mType: TextView? = null
    private var mBalance: TextView? = null


    init {
        mName = itemView.findViewById(R.id.account_name_text)
        mType= itemView.findViewById(R.id.account_type_text)
        mBalance = itemView.findViewById(R.id.account_balance_text)

    }

    fun bind(account: Account) {
        mName?.text = account.name
        mType?.text = account.type
        mBalance?.text = account.balance.toString() + " RON"

    }

}