package com.anish.aidl_sensor.model

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import androidx.lifecycle.MutableLiveData
import com.anish.aidl_sensor.MyAidlInterface

class SensorService: Service(),SensorEventListener {

    private val SENSOR_INTERVAL = 8000

    companion object {
        val data = MutableLiveData<FloatArray>()
    }

    private val sensorMgr: SensorManager by lazy {
        getSystemService(SENSOR_SERVICE) as SensorManager
    }

    private val mySensor: Sensor by lazy {
        sensorMgr?.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
    }

    private fun registerSensorListener() = sensorMgr.registerListener(this, mySensor, SENSOR_INTERVAL)


    private val myBinder: MyAidlInterface.Stub = object:MyAidlInterface.Stub(){

        override fun getSensorData(): String {
            registerSensorListener()
            return data.value?.toString()?:"Data not found"
        }

    }

    override fun onBind(p0: Intent?): IBinder? = myBinder

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_ROTATION_VECTOR) {
                data.value = it.values
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

}