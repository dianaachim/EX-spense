package com.example.ex_spences

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ex_spences.Model.AppExpense
import com.squareup.picasso.Picasso


class ExpensesAdapter(mainContext : Context, private val list: ArrayList<AppExpense>) : RecyclerView.Adapter<ExpensesViewHolder>() {
    var mContext = mainContext


    private fun showMessage(message: String) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpensesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ExpensesViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int = list.size


    override fun onBindViewHolder(holder: ExpensesViewHolder, position: Int) {

        val expense = list[holder.adapterPosition]
        holder.bind(mContext, expense)

    }
}

class ExpensesViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(inflater.inflate(R.layout.expense_details_card, parent, false)) {
    private var mPlace: TextView? = null
    private var mAccount: TextView? = null
    private var mDate: TextView? = null
    private var mDetails: TextView? = null
    private var mAmount: TextView? = null
    private var mCategory: ImageView? = null


    init {
        mPlace = itemView.findViewById(R.id.expense_place_text)
        mAccount = itemView.findViewById(R.id.expense_account_text)
        mDate = itemView.findViewById(R.id.expense_date_text)
        mDetails = itemView.findViewById(R.id.expense_details_text)
        mCategory = itemView.findViewById(R.id.expense_category_image)
        mAmount = itemView.findViewById(R.id.expense_amount)
    }

    private fun loadImageWithPicasso(context: Context, url: String, imageView: ImageView?) {
        Picasso.with(context).load(url).into(imageView)
    }

    fun bind(context: Context, appExpense: AppExpense) {
        mPlace?.text = appExpense.place
        mDetails?.text = appExpense.details
        mAmount?.text = appExpense.amount.toString() + " RON"
        mDate?.text = appExpense.date.toString()
        mAccount?.text = appExpense.account
        loadImageWithPicasso(context, appExpense.categoryImage, mCategory)

    }

}