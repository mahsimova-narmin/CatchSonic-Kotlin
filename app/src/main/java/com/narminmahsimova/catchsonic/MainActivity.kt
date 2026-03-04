package com.narminmahsimova.catchsonic

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.narminmahsimova.catchsonic.databinding.ActivityMainBinding
import java.lang.Runnable


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var runnable: Runnable = Runnable{}
    private var handler: Handler = Handler(Looper.getMainLooper())

    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        object: CountDownTimer(20000,1000){
            override fun onFinish() {
                handler.removeCallbacks(runnable)

                val prefs = getSharedPreferences("com.narminmahsimova.catchsonic", MODE_PRIVATE)

                val highScore = prefs.getInt("highScore", 0)
                if (score > highScore) {
                    prefs.edit().putInt("highScore", score).apply()
                }

                val alert = AlertDialog.Builder(this@MainActivity)
                alert.setTitle("Game Over!")
                alert.setMessage("Play Again?")
                alert.setPositiveButton("Yes",object: DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        recreate()
                    }
                })
                alert.setNegativeButton("No"){p0,p1 ->
                    Toast.makeText(this@MainActivity,"The Game is Over!",Toast.LENGTH_LONG).show()
                    val intent = Intent(applicationContext, ResultsActivity::class.java)
                    intent.putExtra("Score", score)
                    startActivity(intent)
                    finish()
                }
                alert.show()
            }

            override fun onTick(p0: Long) {
                binding.timeView.text = "Time left: ${p0/1000}"
            }

        }.start()

        val images = listOf(binding.imageView1, binding.imageView2, binding.imageView3, binding.imageView4, binding.imageView5, binding.imageView6, binding.imageView7, binding.imageView8, binding.imageView9)

        runnable = object: Runnable{
            override fun run() {
                for (img in images) img.visibility = View.INVISIBLE
                images.random().visibility = View.VISIBLE
                handler.postDelayed(this,1000)
            }
        }
        handler.post(runnable)

    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    fun score(view: View){
        if (view.visibility == View.VISIBLE) {
            score++
            binding.scoreView2.text = "Score: $score"
            view.visibility = View.INVISIBLE
        }
    }
}