package com.example.ex_spences

//import android.content.Context
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ArrayAdapter
//import android.widget.TextView
//import com.example.ex_spences.Model.Account
//
//
//class AccountsSpinAdapter(
//    context: Context, textViewResourceId: Int,
//    values: ArrayList<Account>
//) : ArrayAdapter<Account>(context, textViewResourceId, values) {
//
//    override fun getCount(): Int {
//        return values
//    }
//
//    override fun getItem(position: Int): Account {
//        return values[position]
//    }
//
//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }
//
//    // And the "magic" goes here
//    // This is for the "passive" state of the spinner
//    fun ViewGroup?.getView(position: Int, convertView: View?): View {
//        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
//        val label = super.getView(position, convertView, this!!) as TextView
//        label.setTextColor(Color.BLACK)
//        // Then you can get the current item using the values array (Users array) and the current position
//        // You can NOW reference each method you has created in your bean object (User class)
//        label.setText(values[position].getName())
//
//        // And finally return your dynamic (or custom) view for each spinner item
//        return label
//    }
//
//    // And here is when the "chooser" is popped up
//    // Normally is the same view, but you can customize it if you want
//    override fun getDropDownView(
//        position: Int, convertView: View?,
//        parent: ViewGroup?
//    ): View {
//        val label = super.getDropDownView(position, convertView, parent) as TextView
//        label.setTextColor(Color.BLACK)
//        label.setText(values[position].getName())
//        return label
//    }
//
//    init {
//        this.context = context
//        this.values = values
//    }
//}