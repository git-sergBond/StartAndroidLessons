package com.example.univerlabs_new_3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {

    private val calculator = CalculatorDEC()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnTripleZero.setOnClickListener {
            (1..3).forEach {
                showResult(calculator.putNum(0))
            }
        }

        //TODO del dublicate (liveData) + make enum with operators
        btnZero.setOnClickListener { showResult(calculator.putNum(0)) }
        btnNum1.setOnClickListener { showResult(calculator.putNum(1)) }
        btnNum2.setOnClickListener { showResult(calculator.putNum(2)) }
        btnNum3.setOnClickListener { showResult(calculator.putNum(3)) }
        btnNum4.setOnClickListener { showResult(calculator.putNum(4)) }
        btnNum5.setOnClickListener { showResult(calculator.putNum(5)) }
        btnNum6.setOnClickListener { showResult(calculator.putNum(6)) }
        btnNum7.setOnClickListener { showResult(calculator.putNum(7)) }
        btnNum8.setOnClickListener { showResult(calculator.putNum(8)) }
        btnNum9.setOnClickListener { showResult(calculator.putNum(9)) }
        btnDot.setOnClickListener { showResult(calculator.putDot()) }
        btnCalculate.setOnClickListener { showResult(calculator.calculate()) }
        btnSum.setOnClickListener { showResult(calculator.putOperator('+'))}
        btnSub.setOnClickListener { showResult(calculator.putOperator('-'))}
        btnMul.setOnClickListener { showResult(calculator.putOperator('*'))}
        btnDiv.setOnClickListener { showResult(calculator.putOperator('/'))}
        btnPercent.setOnClickListener { showResult(calculator.putOperator('%'))}
        btnClear.setOnClickListener { showResult(calculator.clear()) }
        btnDelete.setOnClickListener { showResult(calculator.deleteChar()) }
    }

    private fun showResult( s : String) {
        result.text = s
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Store(calculator).saveTo(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Store(calculator).loadFrom(savedInstanceState)
        super.onRestoreInstanceState(savedInstanceState)
    }

}

//TODO live Data
//TODO TDD
//TODO refactoring

//TODO rename on expression
class CalculatorDEC {

    private val expressionDef = "0"
    var expression : String = expressionDef
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
            val matchNum = Regex(Num+'$').find(expression)
            val percent = matchNum!!.value.toFloat() * 0.01
            expression = expression.replaceRange(matchNum.range, percent.toString())
            return expression
        }

        if(getLastChar(1) == '*' && operator == '-'){
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



    //TODO dedicate to class FindUtil
    private val Num = """\d+[.]*\d*"""
    private val findMulDiv = Num + """[*|/][-]?""" + Num
    private val findSubSum = Num + """[+|-][-]?""" + Num

    //TODO divide calc and expression

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

    fun calculate () : String {
        //-pre num
        //if expression not complete delete all not writed
        var temp = expression

        Log.d("asd", "---$temp---")

        var i = 1

        temp = checkToken(temp)

        while (Regex(findMulDiv).containsMatchIn(temp)) {
                val act = Regex(findMulDiv).find(temp)
                val curOper = act!!.value
                val a = Regex(Num).find(curOper)!!.value.toBigDecimal()
                val b = Regex("[-]?"+Num+"$").find(curOper)!!.value.toBigDecimal()
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

        while (Regex(findSubSum).containsMatchIn(temp)) {
                val act = Regex(findSubSum).find(temp)
                val curOper = act!!.value
                val a = Regex(Num).find(curOper)!!.value.toBigDecimal()
                val b = Regex(Num+"$").find(curOper)!!.value.toBigDecimal()
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
        //TODO save to result expression (and show result after = )
        expression = temp
        return temp
    }

}

//TODO add class to validate expression
//TODO create saving history (with multi chekbox - del, */-+ +) animate + choose font + make uncommon color at operator btn and calc btn
class Store(private val calculator: CalculatorDEC) {
    private val expressionSaveKey = "store.activity.calculator.expression.save.key"

    fun saveTo (bundle: Bundle) {
        bundle.putString(expressionSaveKey, calculator.expression)
    }

    fun loadFrom (bundle: Bundle) {
        val loaded = bundle.getString(expressionSaveKey, "Error")
        if(loaded != "Error") {
            calculator.expression = loaded
        }
    }
}