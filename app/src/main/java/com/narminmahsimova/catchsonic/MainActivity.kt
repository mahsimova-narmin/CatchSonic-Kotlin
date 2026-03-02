package com.narminmahsimova.catchsonic

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
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


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    var runnable: Runnable = Runnable{}
    var handler: Handler = Handler(Looper.getMainLooper())

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

        sharedPreferences = this.getSharedPreferences("com.narminmahsimova.catchsonic",MODE_PRIVATE)
        score = sharedPreferences.getInt("score",0)

        object: CountDownTimer(5000,1000){
            override fun onFinish() {
                val alert = AlertDialog.Builder(this@MainActivity)
                alert.setTitle("Game over!")
                alert.setMessage("Restart playing?")
                alert.setPositiveButton("Yes",object: DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        recreate()
                    }
                })
                alert.setNegativeButton("No"){p0,p1 ->
                    Toast.makeText(this@MainActivity,"The game is over!",Toast.LENGTH_LONG).show()
                    finish()
                    val intent = Intent(applicationContext, ResultsActivity::class.java)
                    intent.putExtra("Score", score)
                    startActivity(intent)
                }
                alert.show()
            }

            override fun onTick(p0: Long) {
                binding.timeView.text = "Time left: ${p0/1000}"
            }

        }.start()

    }

    fun score(view: View){
        val images = listOf(binding.imageView1, binding.imageView2, binding.imageView3, binding.imageView4, binding.imageView5, binding.imageView6, binding.imageView7, binding.imageView8, binding.imageView9)
        for (img in images){
            val i = (0 until images.size).random()
            images[i].visibility = View.VISIBLE
        }
        if (view.visibility == View.VISIBLE) {
            score++
            binding.scoreView2.text = "Score: $score"
        }

    }


}