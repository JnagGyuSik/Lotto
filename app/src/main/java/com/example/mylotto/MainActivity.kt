package com.example.mylotto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ReportFragment.Companion.reportFragment

class MainActivity : AppCompatActivity() {

    private val clearButton by lazy { findViewById<Button>(R.id.btn_clear) }
    private val addButton by lazy { findViewById<Button>(R.id.btn_add) }
    private val runButton by lazy { findViewById<Button>(R.id.btn_run) }
    private val numPick by lazy { findViewById<NumberPicker>(R.id.np_num) }

    private val numTextViewList : List<TextView> by lazy {
        listOf<TextView>(
            findViewById(R.id.tv_num1),
            findViewById(R.id.tv_num2),
            findViewById(R.id.tv_num3),
            findViewById(R.id.tv_num4),
            findViewById(R.id.tv_num5),
            findViewById(R.id.tv_num6),
        )
    }

    private var didRun = false
    private val pickNumberSet = hashSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //넘버피커 숫자 범위 지정
        numPick.minValue = 1
        numPick.maxValue = 45

        initAddButton()
        initRunButton()
        initClearButton()
    }

    //번호 추가힉 버튼
    private fun initAddButton() {
        addButton.setOnClickListener {
            when{
                //예외처리
                didRun -> showToast("초기화 후에 시도해주세요.")
                pickNumberSet.size >= 5 -> showToast("숫자는 최대 5개까지 선택할 수 있습니다.")
                pickNumberSet.contains(numPick.value) -> showToast("이미 선택된 숫자입니다.")
                else -> {
                    val textView = numTextViewList[pickNumberSet.size]
                    textView.isVisible = true
                    textView.text = numPick.value.toString()
                    setNumBack(numPick.value, textView)
                    pickNumberSet.add(numPick.value)
                }
            }
        }
    }

    //초기화 버튼
    private fun initClearButton() {
        clearButton.setOnClickListener {
            pickNumberSet.clear()
            numTextViewList.forEach { it.isVisible = false }
            didRun = false
            numPick.value = 1
        }
    }

    //자동 생성 시작 버튼
    private fun initRunButton() {
        runButton.setOnClickListener {
            val list = getRandom()

            didRun = true

            list.forEachIndexed { index, number ->
                val textView = numTextViewList[index]
                textView.text = number.toString()
                textView.isVisible = true
                setNumBack(number, textView)
            }
        }
    }

    private fun getRandom(): List<Int> {
        val  number = (1 .. 45).filter { it !in pickNumberSet } //사용자가 선택한 숫자를 뺀 숫자
        return (pickNumberSet + number.shuffled().take(6-pickNumberSet.size)).sorted() //오름차순 정렬{사용자가 선택한 숫자 + 랜덤한 숫자(1~45 - 사용자가 선택한 숫자)}
    }


    private fun setNumBack(number:Int, textView: TextView){
        val background = when(number) {
            in 1..10 -> R.drawable.circle_yellow
            in 11..20 -> R.drawable.circle_blue
            in 21..30 -> R.drawable.circle_red
            in 31..40 -> R.drawable.circle_gray
            else -> R.drawable.circle_green
        }
            textView.background = ContextCompat.getDrawable(this, background)
    }

    private fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}