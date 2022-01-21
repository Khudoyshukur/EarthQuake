package com.example.services

import android.content.*
import android.os.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.example.services.databinding.ActivityMainBinding
import com.example.services.service.HelloService
import com.example.services.service.MyBoundService
import com.example.services.service.MyForegroundService
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val executorService: ExecutorService = Executors.newFixedThreadPool(4)

    private var myBoundService: MyBoundService? = null

    private var mBound = false
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName?, binder: IBinder?) {
            myBoundService = (binder as? MyBoundService.MyBinder)?.service ?: return
            mBound = true
        }

        override fun onServiceDisconnected(componentName: ComponentName?) {
            myBoundService = null
            mBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnStartService.setOnClickListener {
            startMyBoundService()
        }

        binding.btnBind.setOnClickListener {
            bindToService()
        }

        binding.btnUnbind.setOnClickListener {
            unbindFromService()
        }

        binding.btnStopService.setOnClickListener {
            stopService(Intent(this, MyBoundService::class.java))
        }
    }

    private fun startMyBoundService() {
        val intent = Intent(this, MyBoundService::class.java)
        startService(intent)
    }

    private fun bindToService() {
        val intent = Intent(this, MyBoundService::class.java)
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    private fun unbindFromService() {
        unbindService(connection)
    }

    private fun startMyForegroundService() {
        val intent = Intent(this, MyForegroundService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    private fun startHelloService() {
        val intent = Intent(this, HelloService::class.java)
        intent.putExtras(bundleOf("receiver" to MyReceiver(Handler(Looper.getMainLooper()))))
        startService(intent)
    }

    private inner class MyReceiver(handler: Handler) : ResultReceiver(handler) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            val message = resultData?.getString("extra") ?: "no message"
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}