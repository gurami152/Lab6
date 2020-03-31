package com.example.lab6

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlin.Int
import com.example.lab6.R.layout.activity_main
import kotlinx.android.synthetic.main.activity_main.*
import android.os.CountDownTimer
import android.media.RingtoneManager
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.view.*
import android.widget.*


class MainActivity : AppCompatActivity() {

    private var colorRight : Int = 1
    private var score: Int =0
    private var numberOfQuestions: Int = 0
    private var colorLeftText: Int = 1
    private var colorRightText: Int = 1
    private var colorLeft: Int = 1
    private var soundPlay :Boolean = false
    private var backgroundChange :Boolean=false
    private var playTime:Int= 60000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(activity_main)
        button.setOnClickListener { yesClick() }
        button2.setOnClickListener{ noClick() }
        button3.setOnClickListener{ start() }
        // скрытие перед стартом
        button.visibility=Button.INVISIBLE
        button2.visibility=Button.INVISIBLE
        textView2.visibility=TextView.INVISIBLE
        textView.visibility=TextView.INVISIBLE
        textView4.visibility=TextView.INVISIBLE
        textView5.visibility=TextView.INVISIBLE
        progressBar.visibility=ProgressBar.INVISIBLE
        registerForContextMenu(main)
        val popupMenu = PopupMenu(this, textView6)
        popupMenu.inflate(R.menu.menu_popup)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu1 -> {
                    textView6.text = "10 sec"
                    playTime=10000
                    true
                }
                R.id.menu2 -> {
                    textView6.text = "20 sec"
                    playTime=20000
                    true
                }
                R.id.menu3 -> {
                    textView6.text = "30 sec"
                    playTime=30000
                    true
                }
                R.id.menu4 -> {
                    textView6.text = "40 sec"
                    playTime=40000
                    true
                }
                R.id.menu5 -> {
                    textView6.text = "350 sec"
                    playTime=50000
                    true
                }
                R.id.menu6 -> {
                    textView6.text = "60 sec"
                    playTime=60000
                    true
                }
                else -> false
            }
        }
        textView6.setOnClickListener{
            popupMenu.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_exit -> {
            finish()
            return true
            }
            R.id.action_sound -> {
                if(item.isChecked){
                    soundPlay=false
                    item.isChecked=false
                }
                else{
                    soundPlay=true
                    item.isChecked = true
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_context, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_background -> {
                if(item.isChecked) {
                    backgroundChange=false
                    item.isChecked=false
                }
                else{
                    backgroundChange=true
                    item.isChecked=true
                }
                return true
            }

        }
        return super.onContextItemSelected(item)
    }

    fun start(){
        button3.visibility=Button.INVISIBLE
        textView7.visibility=TextView.INVISIBLE
        textView6.visibility=TextView.INVISIBLE
        button.visibility=Button.VISIBLE
        button2.visibility=Button.VISIBLE
        textView2.visibility=TextView.VISIBLE
        textView.visibility=TextView.VISIBLE
        textView4.visibility=TextView.VISIBLE
        textView5.visibility=TextView.VISIBLE

        generateColors()
        val timer = object: CountDownTimer(playTime.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                textView2.text = "Времени осталось 0:" + millisUntilFinished / 1000
            }

            override fun onFinish() {results()}
        }
        timer.start()
    }

    fun results(){

        if(soundPlay) {
            try {
                val notify = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val r = RingtoneManager.getRingtone(applicationContext, notify)
                r.play()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        val intent = Intent(this, ResultsActivity::class.java).apply {
            putExtra("text", "Было задано $numberOfQuestions вопросов из которых Вы правильно ответили на $score")
            putExtra("Email",message)
        }
        startActivity(intent)
    }


    private fun noClick(){
        numberOfQuestions++
        if(colorRight!=colorLeft){
            score++
        }
        generateColors()
    }

    private fun yesClick(){
        numberOfQuestions++
        if(colorRight==colorLeftText){
            score++
        }
        generateColors()
    }

    private fun rand(from: Int, to: Int) : Int {
        return (from..to).random()
    }

    @SuppressLint("ResourceType", "Recycle")
    fun generateColors() {

        val colors: TypedArray = resources.obtainTypedArray(R.array.rainbow)
        val strings: Array<out String> = resources.getStringArray(R.array.color_name)
        var color=rand(0,6)
        textView4.setTextColor(colors.getColor(color,0))
        colorLeft=color

        color = rand(0,6)
        textView4.text = strings[color]
        colorLeftText=color

        color = rand(0,6)
        textView5.setTextColor(colors.getColor(color,0))
        colorRight=color

        color = rand(0,6)
        textView5.text = strings[color]
        colorRightText=color
        if(backgroundChange) {
            while ((color == colorRight) || (color == colorLeft)) {
                color = rand(0, 6)
            }
            main.setBackgroundColor(colors.getColor(color, 0))
        }

    }


}

