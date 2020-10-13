package com.example.ex_spences

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ex_spences.Model.*
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.add_income_card.view.*
import kotlinx.android.synthetic.main.add_savings_card.view.*
import kotlinx.android.synthetic.main.fragment_add_expense.view.*
import kotlinx.android.synthetic.main.fragment_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToInt
import kotlin.math.roundToLong


class MainFragment : Fragment() {
    lateinit var mFirebaseAuth: FirebaseAuth
    private var mExpenses = ArrayList<Expense>()
    private var mAppExpenses = ArrayList<AppExpense>()
    private var mAccounts = ArrayList<Account>()
    private var mCategories = ArrayList<Category>()
    private var mBudgets = ArrayList<Budget>()
    private var mAllBudgets = ArrayList<Budget>()
    private var mSavings = ArrayList<PiggyBank>()
    private var mPiggyBankId = ""
    private var mPiggyBankValue = 0

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

    fun getUserAccounts(callback: AccountsListCallback) {
        val user = FirebaseAuth.getInstance().uid
        val db = Firebase.firestore
        if (user != null) {
            db.collection("Accounts")
                .whereEqualTo("uid", user)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val accounts = ArrayList<Account>()
                        for (document in task.result!!) {
                            val name = document.get("name") as String
                            val balance = document.get("balance") as Double
                            val aid = document.get("aid") as String
                            val type = document.get("type") as String
                            val uid = document.get("uid") as String
                            val account = Account(aid, name, type, balance, uid)
                            accounts.add(account)
                        }
                        callback.onCallback(accounts) // callback la metoda din interfata dupa ce se termina de luat toate entitatile
                    } else {
                        Toast.makeText(context, task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    fun getCategories(callback: CategoryListCallback) {
        val db = Firebase.firestore
        db.collection("Categories")
            .get()
            .addOnCompleteListener{task ->
                if (task.isSuccessful) {
                    val categories = ArrayList<Category>()
                    for (document in task.result!!) {
                        val cid = document.get("cid") as String
                        val name = document.get("name") as String
                        val icon = document.get("icon") as String
                        val category = Category(cid, name, icon)
                        categories.add(category)
                    }
                    callback.onCallback(categories)
                } else {
                    showMessage(task.exception!!.message.toString())
                }
            }
    }

    fun getExpenseCategory(cid: String, callback: CategoryListCallback) {
        val db = Firebase.firestore
        db.collection("Categories")
            .whereEqualTo("cid", cid)
            .get()
            .addOnCompleteListener{task ->
                if (task.isSuccessful) {
                    val categories = ArrayList<Category>()
                    for (document in task.result!!) {
                        val cid = document.get("cid") as String
                        val name = document.get("name") as String
                        val icon = document.get("icon") as String
                        val category = Category(cid, name, icon)
                        categories.add(category)
                    }
                    callback.onCallback(categories)
                } else {
                    showMessage(task.exception!!.message.toString())
                }
            }
    }

    fun getExpenseAccount(aid: String, callback: AccountsListCallback) {
        val db = Firebase.firestore
        if (aid != null) {
            db.collection("Accounts")
                .whereEqualTo("aid", aid)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val accounts = ArrayList<Account>()
                        for (document in task.result!!) {
                            val name = document.get("name") as String
                            val balance = document.get("balance") as Double
                            val aid = document.get("aid") as String
                            val type = document.get("type") as String
                            val uid = document.get("uid") as String
                            val account = Account(aid, name, type, balance, uid)
                            accounts.add(account)
                        }
                        callback.onCallback(accounts) // callback la metoda din interfata dupa ce se termina de luat toate entitatile
                    } else {
                        Toast.makeText(context, task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    fun getUserExpenses(callback: ExpensesListCallback) {
        val user = FirebaseAuth.getInstance().uid
        val db = Firebase.firestore
        if (user != null) {
            db.collection("Expenses")
                .whereEqualTo("uid", user)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val expenses = ArrayList<Expense>()
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
                            expenses.add(expense)
                        }
                        callback.onCallback(expenses) // callback la metoda din interfata dupa ce se termina de luat toate entitatile
                    } else {
                        Toast.makeText(context, task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                        Log.w("MainFragment", task.exception!!.message.toString())
                    }
                }
        }
    }

    fun getUserBudgetsForCurrentYear(callback: BudgetsListCallback) {
        val user = FirebaseAuth.getInstance().uid
        val db = Firebase.firestore
        val c = Calendar.getInstance()
        val current_year = c.get(Calendar.YEAR)
        if (user != null) {
            db.collection("Budgets")
                .whereEqualTo("uid", user)
                .whereEqualTo("year", current_year)
                .get()
                .addOnCompleteListener{task ->
                    if (task.isSuccessful) {
                        val budgets = ArrayList<Budget>()
                        for (document in task.result!!) {
                            val amount = document.get("amount") as Long
                            val current_amount = document.get("current_amount") as Double
                            val month = document.get("month") as Long
                            val uid = document.get("uid") as String
                            val year = document.get("year") as Long
                            val bid = document.get("bid") as String
                            val budget = Budget(bid, amount, current_amount, month, year, uid)
                            budgets.add(budget)
                        }
                        callback.onCallback(budgets)
                    }

                }
        }
    }

    fun getUserBudgets(callback: BudgetsListCallback) {
        val user = FirebaseAuth.getInstance().uid
        val db = Firebase.firestore
        if (user != null) {
            db.collection("Budgets")
                .whereEqualTo("uid", user)
                .get()
                .addOnCompleteListener{task ->
                    if (task.isSuccessful) {
                        val budgets = ArrayList<Budget>()
                        for (document in task.result!!) {
                            val amount = document.get("amount") as Long
                            val current_amount = document.get("current_amount") as Double
                            val month = document.get("month") as Long
                            val uid = document.get("uid") as String
                            val year = document.get("year") as Long
                            val bid = document.get("bid") as String
                            val budget = Budget(bid, amount, current_amount, month, year, uid)
                            budgets.add(budget)
                        }
                        callback.onCallback(budgets)
                    }

                }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = Firebase.firestore
        mFirebaseAuth = FirebaseAuth.getInstance()
        val uid = mFirebaseAuth.currentUser?.uid
        val c = Calendar.getInstance()
        var foundBudget: Boolean = false
        var currentSetBudget = Budget()
        var currentBudgetProgress = 0

        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
        val currentProgress = view.findViewById<TextView>(R.id.current_budget_tv)
        val totalBudget = view.findViewById<TextView>(R.id.total_budget_tv)

        getUserBudgetsForCurrentYear(object : BudgetsListCallback{
            override fun onCallback(value: List<Budget>) {
                mBudgets = value as ArrayList<Budget>
                for (budget in mBudgets) {
                    if (budget.month == (c.get(Calendar.MONTH)+1).toLong()) {
                        progressBar.max = budget.amount.toInt()
                        progressBar.progress = budget.current_amount.toInt()
                        currentProgress?.text = budget.current_amount.toString()
                        totalBudget?.text = budget.amount.toString()
                        foundBudget = true
                        currentSetBudget = budget
                        currentBudgetProgress = budget.current_amount.toInt()
                        break
                    }
                }

                if (foundBudget == false) {
                    //it means that there is no budget for the current month. Either the user is new, or the previous budget time ended
                    progressBar.progress = 0
                    currentProgress?.text = 0.toString()
                    currentBudgetProgress = 0
                    getUserBudgets(object : BudgetsListCallback {
                        override fun onCallback(value: List<Budget>) {
                            mAllBudgets = value as ArrayList<Budget>
                            if (mAllBudgets.size == 0) {
                                //the user is new. Set default values
                                progressBar.max = 500
                                totalBudget?.text = 500.toString()

                                //add the new budget to the database
                                val id = db.collection("Budgets").document().id
                                val newBudget = Budget(id, 500, 0.0, (c.get(Calendar.MONTH)+1).toLong(), (c.get(Calendar.YEAR)).toLong(), uid )
                                db.collection("Budgets")
                                    .document(id)
                                    .set(newBudget)
                                    .addOnSuccessListener { documentReference ->
                                        Log.w("MainFragment", "budget added successfully")
                                        showMessage("Budget added")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w("MainFragment", "error adding budget")
                                    }

                            } else{
                                //we take the budget from the previous month
                                getUserBudgetsForCurrentYear(object : BudgetsListCallback{
                                    override fun onCallback(value: List<Budget>) {
                                        mBudgets = value as ArrayList<Budget>
                                        for (budget in mBudgets) {
                                            if (budget.month == c.get(Calendar.MONTH).toLong()) {
                                                val newAmount = ((budget.amount + budget.current_amount)/2).roundToLong()

                                                //for the savings part
                                                if (budget.amount > budget.current_amount){
                                                    val newPiggyBankValue = mPiggyBankValue + budget.amount-budget.current_amount
                                                    db.collection("PiggyBank")
                                                        .document(mPiggyBankId)
                                                        .update("value", newPiggyBankValue)
                                                } else {
                                                    val newPiggyBankValue = mPiggyBankValue - (budget.current_amount - budget.amount)
                                                    db.collection("PiggyBank")
                                                        .document(mPiggyBankId)
                                                        .update("value", newPiggyBankValue)
                                                }

                                                //set the text views for the progress bar

                                                progressBar.max = newAmount.toInt()
                                                totalBudget?.text = newAmount.toString()
                                                foundBudget = true
                                                currentSetBudget = budget

                                                //add the new budget to the database
                                                val id = db.collection("Budgets").document().id
                                                val newBudget = Budget(id, newAmount, 0.0, (c.get(Calendar.MONTH)+1).toLong(), (c.get(Calendar.YEAR)).toLong(), uid )
                                                db.collection("Budgets")
                                                    .document(id)
                                                    .set(newBudget)
                                                    .addOnSuccessListener { documentReference ->
                                                        Log.w("MainFragment", "budget added successfully")
                                                        showMessage("Budget added")
                                                    }
                                                    .addOnFailureListener { e ->
                                                        Log.w("MainFragment", "error adding budget")
                                                    }
                                                break
                                            }
                                        }
                                    }
                                })
                            }
                        }
                    })
                }

            }
        })



        getUserAccounts(object : AccountsListCallback {
            override fun onCallback(value: List<Account>) {
                mAccounts =
                    value as ArrayList<Account>
            }
        } )

        getCategories(object  : CategoryListCallback {
            override fun onCallback(value: List<Category>) {
                mCategories = value as ArrayList<Category>
            }
        })

        getUserSavings(object : SavingsListCallback{
            override fun onCallback(value: List<PiggyBank>) {
                mSavings = value as ArrayList<PiggyBank>
                if (mSavings.size == 0) {
                    //it means new user
                    val id = db.collection("PiggyBank").document().id
                    val newPiggyBank = PiggyBank(id, 0.0, uid!!)
                    db.collection("PiggyBank")
                        .document(id)
                        .set(newPiggyBank)
                        .addOnSuccessListener {
                            Log.w("MainFragment", "piggy bank added successfully")
                            showMessage("Piggy bank added succssesfully")
                            mPiggyBankId = id
                            mPiggyBankValue = 0
                            mSavings.add(newPiggyBank)

                        }
                        .addOnFailureListener { e ->
                            Log.w("MainFragment", "error adding budget")
                        }
                } else {
                    mPiggyBankId = mSavings.get(0).pgid
                    mPiggyBankValue = mSavings.get(0).value.toInt()
                }
            }
        })

        getUserExpenses(object : ExpensesListCallback {
            override fun onCallback(value: List<Expense>) {
                mExpenses = value as ArrayList<Expense>
                for (expense in mExpenses) {
                    var categoryImage = ""
                    var accountName = ""
                    getExpenseCategory(expense.cid, object : CategoryListCallback {
                        override fun onCallback(value: List<Category>) {
                            categoryImage = value.get(0).icon
                            getExpenseAccount(expense.aid, object : AccountsListCallback{
                                override fun onCallback(value: List<Account>) {
//                                    showMessage(value.size.toString())
                                    accountName = value.get(0).name
                                    val sdf = SimpleDateFormat("dd/MM/yyyy")
                                    val newDate = sdf.format(Date(expense.date.toString())).toString()
                                    val appExpense = AppExpense(expense.eid, accountName, categoryImage, expense.amount, newDate, expense.details, expense.place, expense.uid)
                                    mAppExpenses.add(appExpense)
                                    expenses_list.apply {
                                        layoutManager = LinearLayoutManager(activity)
                                        adapter = ExpensesAdapter(context, mAppExpenses)
                                        addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                                    }
                                }
                            })
                        }
                    })
                }

            }
        })


        progress_bar.setOnClickListener {
            val inflater = LayoutInflater.from(context)
            val mDialogView =inflater.inflate(R.layout.add_budget_card, null)
            val budgetEditText: EditText = mDialogView.findViewById(R.id.add_budget_text)

            AlertDialog.Builder(activity)
                .setView(mDialogView)
                .setTitle("Set new budget")
                .setPositiveButton("Create", DialogInterface.OnClickListener { _, _ ->
                    val c = Calendar.getInstance()
                    val year = c.get(Calendar.YEAR).toLong()
                    val month = (c.get(Calendar.MONTH)+1).toLong()
                    val newBudget = budgetEditText.text.toString()

                    if (newBudget == "") {
                        Toast.makeText(activity, "Add budget!", Toast.LENGTH_SHORT).show()
                    } else {
                        if (currentSetBudget.month == month) {
                            db.collection("Budgets")
                                .document(currentSetBudget.bid)
                                .update("amount", newBudget.toLong())
                            progress_bar.max = newBudget.toInt()
                            totalBudget?.text = newBudget
                            currentSetBudget.amount = newBudget.toLong()
                        } else {
                            val id = db.collection("Budgets").document().id
                            val newSetBudget: Budget = Budget(id, newBudget.toLong(), 0.0, month, year, uid)
                            db.collection("Budgets")
                                .document(id)
                                .set(newSetBudget)
                                .addOnSuccessListener {
                                    Log.w("MainFragment", "budget added successfully")
                                    showMessage("Budget added succssesfully")
                                    currentSetBudget = newSetBudget

                                }
                                .addOnFailureListener { e ->
                                    Log.w("MainFragment", "error adding budget")
                                }
                            progress_bar.max = newBudget.toInt()
                            totalBudget?.text = newBudget
                            currentSetBudget.amount = newBudget.toLong()
                        }
                    }
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener{ _, _-> })
                .create()
                .show()
        }

        add_expense_button.setOnClickListener {
            val inflater = LayoutInflater.from(context)
            val mDialogView =inflater.inflate(R.layout.fragment_add_expense, null)
            val accountSpinner: Spinner = mDialogView.findViewById(R.id.add_expense_account_spinner)
            val categorySpinner: Spinner = mDialogView.findViewById(R.id.add_expense_category_spinner)
            val datePicker: DatePicker = mDialogView.findViewById(R.id.add_expense_date_picker)

            val accountsAdapter = ArrayAdapter(
                activity!!,
                android.R.layout.simple_spinner_item, mAccounts
            )
            accountsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            accountSpinner.adapter = accountsAdapter

            val categoriesAdapter = ArrayAdapter(
                activity!!, android.R.layout.simple_spinner_item, mCategories
            )
            categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categorySpinner.adapter = categoriesAdapter

            var account: Account = Account()
            var category: Category = Category()
            var oldBalance:Double = 0.0

            accountSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    showMessage("Please select an account")
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    account = accountsAdapter.getItem(position)!!
                    oldBalance = account.balance
                }

            }

            categorySpinner.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    showMessage("Please select a category")
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    category = categoriesAdapter.getItem(position)!!
                }
            }

            AlertDialog.Builder(activity)
                .setView(mDialogView)
                .setTitle("Add expense")
                .setPositiveButton("Create", DialogInterface.OnClickListener{ _, _->
                    val newPlace = mDialogView.add_expense_place_edit_text.text.toString()
                    val newAmount = mDialogView.add_expense_amount_text.text.toString()
                    val newDetails = mDialogView.add_expense_details_text.text.toString()

                    if (newAmount == "" ) {
                        Toast.makeText(activity,"Add expense cost!", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        if (oldBalance < newAmount.toDouble()) {
                            showMessage("Not enough funds! Please select another account")
                        }
                        else {

                            val date123 = Date(datePicker.year-1900, datePicker.month, datePicker.dayOfMonth)
                            val sdf = SimpleDateFormat("dd/MM/yyyy")
                            val newDate = sdf.format(date123).toString()

                            val id = db.collection("Expenses").document().id
                            val newExpense = Expense(id,
                                account.aid!!, category.cid, newAmount.toDouble(), date123, newDetails, newPlace, uid)
                            val newAppExpense = AppExpense(id, account.name, category.icon, newAmount.toDouble(), newDate, newDetails, newPlace, uid)

                            db.collection("Expenses")
                                .document(id)
                                .set(newExpense)
                                .addOnSuccessListener { documentReference ->
                                    Log.w("MainFragment", "expense added successfully")
                                    showMessage("Expense added")
                                }
                                .addOnFailureListener { e ->
                                    Log.w("MainFragment", "error adding expense")
                                }
                            mExpenses.add(newExpense)
                            mAppExpenses.add(newAppExpense)
                            expenses_list.apply {
                                layoutManager = LinearLayoutManager(activity)
                                adapter = ExpensesAdapter(context, mAppExpenses)
                                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                            }
                            expenses_list.adapter?.notifyItemInserted(mExpenses.size+1)
                            expenses_list.adapter?.notifyDataSetChanged()

                            db.collection("Accounts")
                                .document(account.aid!!)
                                .update("balance", oldBalance-newAmount.toDouble())
                            db.collection("Budgets")
                                .document(currentSetBudget.bid)
                                .update("current_amount", currentBudgetProgress.toDouble() + newAmount.toDouble())

                            val new_progress = currentSetBudget.current_amount.roundToInt() + newAmount.toDouble().roundToInt()

                            progress_bar.progress = new_progress
                            currentProgress?.text = (currentSetBudget.current_amount + newAmount.toDouble()).toString()
                        }

                    }
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener{ _, _-> })
                .create()
                .show()
        }

        add_income_button.setOnClickListener {
            val inflater = LayoutInflater.from(context)
            val mDialogView =inflater.inflate(R.layout.add_income_card, null)
            val accountSpinner: Spinner = mDialogView.findViewById(R.id.add_income_account_spinner)

            val adapter = ArrayAdapter(
                activity!!,
                android.R.layout.simple_spinner_item, mAccounts
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            accountSpinner.adapter = adapter

            var account: Account = Account()
            var oldBalance:Double = 0.0

            accountSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    showMessage("Please select an account")
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    account = adapter.getItem(position)!!
                    oldBalance = account.balance
                }

            }

            AlertDialog.Builder(activity)
                .setView(mDialogView)
                .setTitle("Add income")
                .setPositiveButton("Add", DialogInterface.OnClickListener{ _, _->
                    val newAmount = mDialogView.add_income_amount_text.text.toString()


                    if (newAmount == "" ) {
                        Toast.makeText(activity,"Add income amount!", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        db.collection("Accounts")
                            .document(account.aid!!)
                            .update("balance", oldBalance+newAmount.toDouble())

                        val id = db.collection("Incomes").document().id
                        val income = Income(id, newAmount.toLong(), account.aid!!, uid)
                        db.collection("Incomes")
                            .document(id)
                            .set(income)
                            .addOnSuccessListener { documentReference ->
                                Log.w("MainFragment", "income added successfully")
                                showMessage("Income Added")
                            }
                            .addOnFailureListener { e ->
                                Log.w("MainFragment", "error adding income")
                            }
                    }
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener{ _, _-> })
                .create()
                .show()
        }

        add_savings_button.setOnClickListener {
            val inflater = LayoutInflater.from(context)
            val mDialogView =inflater.inflate(R.layout.add_savings_card, null)

            AlertDialog.Builder(activity)
                .setView(mDialogView)
                .setTitle("Add to piggy bank")
                .setPositiveButton("Add", DialogInterface.OnClickListener{ _, _->
                    val savings = mDialogView.add_savings_text.text.toString()
                    if (savings == ""){
                        showMessage("Please enter a value")
                    } else {
                        val oldValue = mSavings.get(0).value
                       db.collection("PiggyBank")
                           .document(mPiggyBankId)
                           .update("value", oldValue+savings.toDouble())
                    }

                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener{ _, _-> })
                .create()
                .show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_main, container, false)

        return view;

    }

}