package com.example.ex_spences

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.ex_spences.Model.Category
import com.example.ex_spences.Model.Expense
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.COLORFUL_COLORS
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class StatisticsFragment : Fragment() {
    lateinit var mFirebaseAuth: FirebaseAuth
    private var mExpenses = ArrayList<Expense>()
    private var thisMonthExpenses = ArrayList<Expense>()
    private var mCategories = ArrayList<Category>()
    private var categoryFrequencies = HashMap<String, Double>()
    private var categoryIdsFrequencies = HashMap<String, Double>()
    lateinit var barChart: BarChart
    private var barEntryList = ArrayList<BarEntry>()
    private var barLabels = ArrayList<String>()
    private var dailyExpensesHashMap = HashMap<Int, Double>()

    private fun showMessage(message:String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    fun getUserExpenses(callback: ExpensesHashMapCallback) {
        //returneaza un hash map cu cid si suma totala e cheltuielilor cu acel cid
        val user = FirebaseAuth.getInstance().uid
        val db = Firebase.firestore
        if (user != null) {
            db.collection("Expenses")
                .whereEqualTo("uid", user)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val expenses = HashMap<String, Double>()
                        for (document in task.result!!) {
                            val place = document.get("place") as String
                            val amount = document.get("amount") as Double
                            val aid = document.get("aid") as String
                            val details = document.get("details") as String
                            val uid = document.get("uid") as String
                            val eid = document.get("eid") as String
                            val cid = document.get("cid") as String
                            val date = document.get("date") as Timestamp
                            val expense = Expense(eid, aid, cid, amount, date.toDate(), details, place, uid)
                            if (expenses.containsKey(cid)){
                                expenses[cid] = expenses[cid]!! + amount
                            } else {
                                expenses[cid] = amount
                            }
                        }
                        callback.onCallback(expenses)
                    } else {
                        Toast.makeText(context, task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    fun getCategoryDetails(hashMap: HashMap<String, Double>, callback: CategoryDetailsCallback) {
        val db = Firebase.firestore
        db.collection("Categories")
            .whereIn("cid", hashMap.keys.toList())
            .get()
            .addOnCompleteListener{task ->
                if (task.isSuccessful) {
                    val categories = HashMap<String, Double>()
                    for (document in task.result!!) {
                        val cid = document.get("cid") as String
                        val name = document.get("name") as String
                        val icon = document.get("icon") as String
                        val category = Category(cid, name, icon)
                        categories[name] = hashMap[cid]!!.toDouble()
                    }

                    callback.onCallback(categories)
                } else {
                    showMessage(task.exception!!.message.toString())
                }
            }
    }

    fun getDailyExpenses(callback: DailyExpensesHashMapCallback) {
        //returneaza un hash map cu data si suma totala a cheltuielilor din ziua aia (numai din luna curenta)
        val user = FirebaseAuth.getInstance().uid
        val db = Firebase.firestore
        val c = Calendar.getInstance()
        val current_year = c.get(Calendar.YEAR)
        val current_month = c.get(Calendar.MONTH) + 1
        if (user != null) {
            db.collection("Expenses")
                .whereEqualTo("uid", user)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val expenses = HashMap<Int, Double>()
                        expenses[1] = 0.0 //first quarter of the month
                        expenses[2] = 0.0 //second quarter of the month etc
                        expenses[3] = 0.0
                        expenses[4] = 0.0
                        for (document in task.result!!) {
                            val place = document.get("place") as String
                            val amount = document.get("amount") as Double
                            val aid = document.get("aid") as String
                            val details = document.get("details") as String
                            val uid = document.get("uid") as String
                            val eid = document.get("eid") as String
                            val cid = document.get("cid") as String
                            val timestamp = document.get("date") as Timestamp
                            val date = timestamp.toDate()
//                            showMessage((date.year).toString() + "/"+(date.month).toString()+ "/"+date.day.toString())
//                            showMessage(date.toString())
                            if (date.year == current_year - 1900 && date.month == current_month-1) {
                                if (date.day<8) {
                                    expenses[4] = expenses[1]!! + amount
                                } else if (date.day >=8 && date.day <=15) {
                                    expenses[3] = expenses[2]!! + amount
                                } else if (date.day >15 && date.day <=23) {
                                    expenses[2] = expenses[3]!! + amount
                                } else {
                                    expenses[1] = expenses[4]!! + amount
                                }
//                                if (expenses.containsKey(date.day)){
//                                    expenses[date.day] = expenses[date.day]!! + amount
//                                } else {
//                                    expenses[date.day] = amount
//                                }
                            }

                        }
                        callback.onCallback(expenses)
                    } else {
                        Toast.makeText(context, task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                    }
                }
        }
    }


    fun setupPieChart() {
        val pieEntries : ArrayList<PieEntry> = ArrayList()
        for (category in categoryFrequencies) {
            pieEntries.add(PieEntry(category.value.toFloat(), category.key.toString()))
        }
        val dataSet : PieDataSet = PieDataSet(pieEntries, "Monthly statistics")
        dataSet.colors = COLORFUL_COLORS.toList()
        val pieData: PieData = PieData(dataSet)

        //get chart
        val chart : PieChart = view!!.findViewById(R.id.pie_chart)
        chart.data = pieData
        chart.animateY(1000)
        chart.invalidate()
    }

    fun setupBarChart() {
        val c = Calendar.getInstance()
        val days: Int = c.getActualMaximum(Calendar.DAY_OF_MONTH)
        barLabels.add("0")
        barLabels.add("1-7")
        barLabels.add("8-15")
        barLabels.add("16-23")
        barLabels.add("24-"+days.toString())


        barEntryList.add(BarEntry(1F, dailyExpensesHashMap[1]!!.toFloat()))
        barEntryList.add(BarEntry(2F, dailyExpensesHashMap[2]!!.toFloat()))
        barEntryList.add(BarEntry(3F, dailyExpensesHashMap[3]!!.toFloat()))
        barEntryList.add(BarEntry(4F, dailyExpensesHashMap[4]!!.toFloat()))



        val barChart : BarChart = view!!.findViewById(R.id.bar_chart)

        val barDataSet: BarDataSet = BarDataSet(barEntryList, "Daily expenses")
        barDataSet.setColors(ColorTemplate.PASTEL_COLORS.toList())
        var description: Description = Description()
        description.text = "Days"
        barChart.description = description
        val barData : BarData = BarData(barDataSet)
        barChart.data = barData
        var xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(barLabels)
        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.granularity = 1f
        xAxis.setLabelCount(days)
//        xAxis.labelRotationAngle = 270F
        barChart.animateY(2000)
        barChart.invalidate()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistics, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFirebaseAuth = FirebaseAuth.getInstance()
        val c = Calendar.getInstance()
        val days: Int = c.getActualMaximum(Calendar.DAY_OF_MONTH)



        //set pie chart

        getUserExpenses(object : ExpensesHashMapCallback{
            override fun onCallback(value: HashMap<String, Double>) {
                categoryIdsFrequencies = value as HashMap<String, Double>
                if (categoryIdsFrequencies.isEmpty()) {
                    AlertDialog.Builder(activity)
                        .setTitle("Empty chart")
                        .setMessage("You do not have any registered expenses yet")
                        .setPositiveButton("OK", null)
                        .show()
                } else {
                    getCategoryDetails(categoryIdsFrequencies, object : CategoryDetailsCallback{
                        override fun onCallback(value: HashMap<String, Double>) {
                            categoryFrequencies = value as HashMap<String, Double>
                            setupPieChart()
                        }
                    })
                }

            }
        })

        //set bar chart



        getDailyExpenses(object : DailyExpensesHashMapCallback{
            override fun onCallback(value: HashMap<Int, Double>) {
                dailyExpensesHashMap = value as HashMap<Int, Double>

                setupBarChart()

            }
        })

    }

}