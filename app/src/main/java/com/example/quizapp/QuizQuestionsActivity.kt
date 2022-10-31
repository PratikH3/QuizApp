package com.example.quizapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat

class QuizQuestionsActivity : AppCompatActivity(), View.OnClickListener{

    private var mCurrentPosition: Int = 1
    private var mQuestionsList: ArrayList<Question>? = null
    private var mSelectedOptionPosition :Int = 0
    private var mCorrectAnswers: Int = 0
    private var mUserName: String ?= null
    private var isAnswered: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_quiz_questions)

        mUserName = intent.getStringExtra(Constants.USER_NAME)

        mQuestionsList = Constants.getQuestions()

        val tv_option_one = findViewById<TextView>(R.id.tv_option_one)
        val tv_option_two = findViewById<TextView>(R.id.tv_option_two)
        val tv_option_three = findViewById<TextView>(R.id.tv_option_three)
        val tv_option_four = findViewById<TextView>(R.id.tv_option_four)

        val btn_submit = findViewById<Button>(R.id.btn_submit)
        val progress_bar = findViewById<ProgressBar>(R.id.progressBar)
        progress_bar.max = mQuestionsList!!.size


        setQuestion(tv_option_one,tv_option_two,tv_option_three,tv_option_four)

        tv_option_one.setOnClickListener(this)
        tv_option_two.setOnClickListener(this)
        tv_option_three.setOnClickListener(this)
        tv_option_four.setOnClickListener(this)
        btn_submit.setOnClickListener(this)


    }
    private fun setQuestion(
        tv_option_one:TextView,
        tv_option_two:TextView,
        tv_option_three:TextView,
        tv_option_four:TextView
    ){

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val tv_progress = findViewById<TextView>(R.id.tv_progress)
        val tv_question = findViewById<TextView>(R.id.tv_question)
        val iv_image = findViewById<ImageView>(R.id.iv_image)
        val btn_submit = findViewById<Button>(R.id.btn_submit)

        isAnswered = false

        val question = mQuestionsList!![mCurrentPosition-1]

        defaultOptionsView(tv_option_one,tv_option_two,tv_option_three,tv_option_four)

        if(mCurrentPosition == mQuestionsList!!.size){
            btn_submit.text = "FINISH"
        }else{
            btn_submit.text = "SUBMIT"
        }

        progressBar.progress = mCurrentPosition
        tv_progress.text = "$mCurrentPosition"+"/"+progressBar.max

        tv_question.text = question!!.question
        iv_image.setImageResource(question.image)
        tv_option_one.text = question.optionOne
        tv_option_two.text = question.optionTwo
        tv_option_three.text = question.optionThree
        tv_option_four.text = question.optionFour
    }

    private fun defaultOptionsView(
        tv_option_one:TextView,
        tv_option_two:TextView,
        tv_option_three:TextView,
        tv_option_four:TextView
    ){

        val options = ArrayList<TextView>()

        options.add(0,tv_option_one)
        options.add(1,tv_option_two)
        options.add(2,tv_option_three)
        options.add(3,tv_option_four)

        for (option in options){
            option.setTextColor(Color.parseColor("#777777"))
            option.typeface = Typeface.DEFAULT
            option.background = ContextCompat.getDrawable(
                this,
                R.drawable.default_option_border_bg
            )
        }
    }

    override fun onClick(v: View?) {
        val tv_option_one = findViewById<TextView>(R.id.tv_option_one)
        val tv_option_two = findViewById<TextView>(R.id.tv_option_two)
        val tv_option_three = findViewById<TextView>(R.id.tv_option_three)
        val tv_option_four = findViewById<TextView>(R.id.tv_option_four)
        val btn_submit = findViewById<Button>(R.id.btn_submit)
            when(v?.id){
                R.id.tv_option_one -> {
                    if(!isAnswered) {
                        selectedOptionView(
                            tv_option_one,
                            1,
                            tv_option_one,
                            tv_option_two,
                            tv_option_three,
                            tv_option_four
                        )
                    }
                }
                R.id.tv_option_two -> {
                    if(!isAnswered) {
                        selectedOptionView(
                            tv_option_two,
                            2,
                            tv_option_one,
                            tv_option_two,
                            tv_option_three,
                            tv_option_four
                        )
                    }
                }
                R.id.tv_option_three -> {
                    if(!isAnswered) {
                        selectedOptionView(
                            tv_option_three,
                            3,
                            tv_option_one,
                            tv_option_two,
                            tv_option_three,
                            tv_option_four
                        )
                    }
                }
                R.id.tv_option_four -> {
                    if(!isAnswered) {
                        selectedOptionView(
                            tv_option_four,
                            4,
                            tv_option_one,
                            tv_option_two,
                            tv_option_three,
                            tv_option_four
                        )
                    }
                }
                R.id.btn_submit -> {
                    isAnswered = true
                    if(mSelectedOptionPosition == 0){
                        mCurrentPosition++
                        when{
                            mCurrentPosition<=mQuestionsList!!.size ->{
                                setQuestion(tv_option_one,tv_option_two,tv_option_three,tv_option_four)
                            }else->{
                                val intent = Intent(this,ResultActivity::class.java)

                                intent.putExtra(Constants.USER_NAME, mUserName)
                                intent.putExtra(Constants.CORRECT_ANSWERS, mCorrectAnswers)
                                intent.putExtra(Constants.TOTAL_QUESTIONS, mQuestionsList!!.size)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }else{
                        val question = mQuestionsList?.get(mCurrentPosition-1)
                        if(question!!.correctAnswer != mSelectedOptionPosition){
                            answerView(mSelectedOptionPosition, R.drawable.wrong_option_border_bg,
                                tv_option_one,tv_option_two,tv_option_three,tv_option_four)
                        }else{
                            mCorrectAnswers++
                        }

                        answerView(question.correctAnswer, R.drawable.correct_option_border_bg,
                            tv_option_one,tv_option_two,tv_option_three,tv_option_four)


                        if(mCurrentPosition==mQuestionsList!!.size){
                            btn_submit.text = "FINISH"
                        }else{
                            btn_submit.text = "GO TO NEXT QUESTION"
                        }
                        mSelectedOptionPosition = 0
                    }
                }

            }
    }

    private fun selectedOptionView(tv: TextView,selectedOptionNum:Int,
                                   tv_option_one:TextView,
                                   tv_option_two:TextView,
                                   tv_option_three:TextView,
                                   tv_option_four:TextView
    ){
        defaultOptionsView(tv_option_one,tv_option_two,tv_option_three,tv_option_four)
        mSelectedOptionPosition = selectedOptionNum

        tv.setTextColor(Color.parseColor("#343A43"))
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.background = ContextCompat.getDrawable(
            this,
            R.drawable.selected_option_border_bg
        )
    }

    private fun answerView(answer: Int,
                           drawableView: Int,
                           tv_option_one:TextView,
                           tv_option_two:TextView,
                           tv_option_three:TextView,
                           tv_option_four:TextView
                           ){
        when(answer){
            1->{
                tv_option_one.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            2->{
                tv_option_two.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            3->{
                tv_option_three.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
            4->{
                tv_option_four.background = ContextCompat.getDrawable(
                    this, drawableView
                )
            }
        }

    }

}