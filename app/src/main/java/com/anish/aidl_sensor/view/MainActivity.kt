package com.anish.aidl_sensor.view

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import com.anish.aidl_sensor.databinding.ActivityMainBinding
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.anish.aidl_sensor.MyAidlInterface
import com.anish.aidl_sensor.R
import com.anish.aidl_sensor.model.SensorService

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private var aidlInterface: MyAidlInterface? = null
    private var serviceConnection: ServiceConnection? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpBinding(savedInstanceState)
        observeSensorData()
        bindService()

    }

    private fun setUpBinding(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    private fun observeSensorData() {
            SensorService.data.observe(
                    this, Observer
                    {
                        binding.tv.text = it.toString()
                    }
            )
    }

    private fun bindService() {
        serviceConnection = object: ServiceConnection {
                override fun onServiceConnected(component: ComponentName?, binder: IBinder?) {
                    aidlInterface = MyAidlInterface.Stub.asInterface(binder)
                }

                override fun onServiceDisconnected(p0: ComponentName?) {

                }
        }

        bindService(Intent(), serviceConnection as ServiceConnection, Context.BIND_AUTO_CREATE)
    }





}