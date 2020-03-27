package com.example.univerlabs_new_4


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigDecimal
import java.math.RoundingMode

//TODO bug in landspace orient (screen on moblephone)
//https://stfalcon.com/ru/blog/post/android-mvvm (Array and animation)
//https://startandroid.ru/ru/courses/architecture-components/27-course/architecture-components/526-urok-3-livedata.html (DataBinding Array)
//TODO live Data(Array) MVVM(Array) dataBinding(Array)
//TODO [create saving history after rotate] (with multi chekbox - del, */-+ +) animate + choose font + make uncommon color at operator btn and calc btn

class MainActivity : AppCompatActivity() {

    private val countOfExpressionsSaveKey = "store.activity.calculator.count.expression.save.key"
    private val expressionSaveKey = "store.activity.calculator.expression.save.key"
    private var historyData : MutableList<String> = ArrayList()
    private lateinit var historyAdapter : HistoryAdapter

    private var currentExpression = ExpressionCalcDEC()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        historyAdapter = HistoryAdapter(historyData, this)
        history.adapter = historyAdapter

        btnTripleZero.setOnClickListener {
            (1..3).forEach {
                showResult(currentExpression.putNum(0))
            }
        }

        btnZero.setOnClickListener { showResult(currentExpression.putNum(0)) }
        btnNum1.setOnClickListener { showResult(currentExpression.putNum(1)) }
        btnNum2.setOnClickListener { showResult(currentExpression.putNum(2)) }
        btnNum3.setOnClickListener { showResult(currentExpression.putNum(3)) }
        btnNum4.setOnClickListener { showResult(currentExpression.putNum(4)) }
        btnNum5.setOnClickListener { showResult(currentExpression.putNum(5)) }
        btnNum6.setOnClickListener { showResult(currentExpression.putNum(6)) }
        btnNum7.setOnClickListener { showResult(currentExpression.putNum(7)) }
        btnNum8.setOnClickListener { showResult(currentExpression.putNum(8)) }
        btnNum9.setOnClickListener { showResult(currentExpression.putNum(9)) }
        btnDot.setOnClickListener { showResult(currentExpression.putDot()) }
        btnSum.setOnClickListener { showResult(currentExpression.putOperator('+'))}
        btnSub.setOnClickListener { showResult(currentExpression.putOperator('-'))}
        btnMul.setOnClickListener { showResult(currentExpression.putOperator('*'))}
        btnDiv.setOnClickListener { showResult(currentExpression.putOperator('/'))}
        btnPercent.setOnClickListener { showResult(currentExpression.putOperator('%'))}
        btnClear.setOnClickListener { showResult(currentExpression.clear()) }
        btnDelete.setOnClickListener { showResult(currentExpression.deleteChar()) }
        btnCalculate.setOnClickListener {
            showResult(CalculatorDEC.calculate(currentExpression))
            addToHistory(currentExpression.expression)
        }
    }

    private fun addToHistory( s : String) {
        history.post {
            historyData.add(s)
            historyAdapter.notifyDataSetChanged()
        }
    }

    private fun showResult( s : String) {
        result.text = s
    }

    override fun onSaveInstanceState(outState: Bundle) {
        //currentExpression
        outState.putString(expressionSaveKey, currentExpression.expression)
        //history
        outState.putString(countOfExpressionsSaveKey, historyData.size.toString())
        for ((i, v) in historyData.withIndex()) {
            outState.putString(expressionSaveKey + i, v)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(bundle: Bundle) {
        //currentExpression
        val currExp = bundle.getString(expressionSaveKey, "0")
        currentExpression = ExpressionCalcDEC(currExp)
        showResult(currExp)
        //history
        val cnt = try { bundle.getString(countOfExpressionsSaveKey, "Error").toInt() } catch ( e : NumberFormatException ) { 0 }
        if(cnt > 0) {
            historyData.clear()
            for(i in 0 until(cnt)) {
                val str = bundle.getString(expressionSaveKey + i, "Error")
                if (str != "Error") {
                    addToHistory(str)
                    Log.d("asda", str)
                }
            }
        }
        super.onRestoreInstanceState(bundle)
    }

}

class HistoryAdapter(var array: MutableList<String>, context: Context) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.expression_item, null, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = array[position]
    }

    override fun getItemCount(): Int {
        return array.size
    }

    class ViewHolder : RecyclerView.ViewHolder {
        var textView : TextView
        constructor(itemView : View) : super(itemView) {
            textView = itemView.findViewById(R.id.textView)
        }
    }
}

object FindHelperDEC {
    const val Num = """\d+[.]*\d*"""
    const val findMulDiv = Num + """[*|/][-]?""" + Num
    const val findSubSum = Num + """[+|-][-]?""" + Num
}

object CalculatorDEC {

    fun checkToken (inExpr: String) : String {
        var i = 0
        var temp = inExpr
        val SequenceSumAndSub = Regex("""[+|-]{2,}""")
        while (SequenceSumAndSub.containsMatchIn(temp)) {
            val act = SequenceSumAndSub.find(temp)
            var o = "-"
            val str = act!!.value
            val cntSub = str.filter { it == '-' }.count()
            if (cntSub % 2 == 0) o = ""
            temp = temp.replaceRange(act.range, o)
            Log.d("asd","[${i++}] $temp / ${act.value} = $o / ${act.range} " )
        }
        return temp
    }

    fun calculate (expression : ExpressionCalcDEC) : String {
        //-pre num
        //if expression not complete delete all not writed
        var temp = expression.expression

        Log.d("asd", "---$temp---")

        var i = 1

        temp = checkToken(temp)

        while (Regex(FindHelperDEC.findMulDiv).containsMatchIn(temp)) {
            val act = Regex(FindHelperDEC.findMulDiv).find(temp)
            val curOper = act!!.value
            val a = Regex(FindHelperDEC.Num).find(curOper)!!.value.toBigDecimal()
            val b = Regex("[-]?"+FindHelperDEC.Num+"$").find(curOper)!!.value.toBigDecimal()
            val o = Regex("""[*|/]""").find(curOper)!!.value
            var res = BigDecimal(0)
            when(o) {
                "*" -> res = a * b
                "/" -> res = a.divide(b, 6, RoundingMode.CEILING).stripTrailingZeros()
            }
            temp = temp.replaceRange(act.range, res.toString())
            Log.d("asd","(${i++}) $temp / ${act.value} = ${res} / ${act.range} / a[$a] o[$o] b[$b] / " )

        }

        temp = checkToken(temp)

        while (Regex(FindHelperDEC.findSubSum).containsMatchIn(temp)) {
            val act = Regex(FindHelperDEC.findSubSum).find(temp)
            val curOper = act!!.value
            val a = Regex(FindHelperDEC.Num).find(curOper)!!.value.toBigDecimal()
            val b = Regex(FindHelperDEC.Num+"$").find(curOper)!!.value.toBigDecimal()
            val o = Regex("""[+|-][-]?""").find(curOper)!!.value
            var res = BigDecimal(0)
            when(o) {
                "+" -> res = a + b
                "-" -> res = a - b
                "+-" -> res = a - b
                "--" -> res = a + b
            }
            temp = temp.replaceRange(act.range, res.toString())
            Log.d("asd","(${i++}) $temp / ${act.value} = ${res} / ${act.range} / a[$a] o[$o] b[$b] / " )
        }

        expression.expression = temp
        return temp
    }
}



class ExpressionCalcDEC(var expression : String = "0")  {

    private val expressionDef = "0"
    private val allOperators = arrayOf('-','+','*','/','%')

    //helperFunc

    private fun getLastChar(i : Int) : Char {
        return expression[expression.length - i]
    }

    private fun delLastChars(i : Int) : String {
        return expression.substring(0, expression.length - i)
    }

    //input

    fun clear() : String {
        expression = expressionDef
        return expression
    }

    fun deleteChar() : String {
        if(expression.length > 1) expression = delLastChars(1)
        else clear()
        return expression
    }

    fun putNum(num : Int) : String {
        val numStr = num.toString()

        if (expression == expressionDef) {
            expression = numStr
            return expression
        }

        if (expression.length >= 2) {
            if (getLastChar(1) == '0' && num == 0 && getLastChar(2) in allOperators) {
                return expression
            }
            if (getLastChar(1) == '0' && getLastChar(2) in allOperators) {
                expression = "${delLastChars(1)}$numStr"
                return expression
            }
        }

        expression += numStr
        return expression
    }

    fun putOperator(operator: Char) : String {

        if (expression == expressionDef) {
            if (operator != '-') {
                return expression
            } else {
                expression = "-$expressionDef"
                return expression
            }
        }

        if(getLastChar(1) !in allOperators && operator == '%') {
            val matchNum = Regex(FindHelperDEC.Num+'$').find(expression)
            val percent = matchNum!!.value.toFloat() * 0.01
            expression = expression.replaceRange(matchNum.range, percent.toString())
            return expression
        }

        if((getLastChar(1) == '*' || getLastChar(1) == '/') && operator == '-'){
            expression += operator
            return expression
        }

        if (getLastChar(1) in allOperators) {
            return expression
        }

        if (getLastChar(1) == '.') {
            expression = "${delLastChars(1)}$operator"
            return expression
        }

        expression += operator
        return expression
    }

    fun putDot() : String {

        if (getLastChar(1) in allOperators) {
            expression = "${expression}0."
            return expression
        }

        //если уже вводят дробное число то запретить повторный ввод запятой
        if (Regex("""[.]\d*$""").containsMatchIn(expression)) {
            return expression
        }

        expression += '.'
        return expression
    }
}