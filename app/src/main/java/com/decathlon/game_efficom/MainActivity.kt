package com.decathlon.game_efficom

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Math.sqrt
import java.net.URISyntaxException
import kotlin.math.sqrt

class MainActivity : AppCompatActivity(), SensorEventListener {
    private var mSocket: Socket? = null

    init {
        try {
            mSocket = IO.socket("http://10.0.2.2:3000/")
        } catch (e: URISyntaxException) {
            gyroMagn.text = e.message
        }

    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    private val NS2S = 1.0f / 1000000000.0f
    private val deltaRotationVector = FloatArray(4) { 0f }
    private var timestamp: Float = 1f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        sensorManager!!.registerListener(this, sensor, SensorManager.AXIS_X)

        mSocket!!.connect()


    }
    override fun onSensorChanged(event: SensorEvent?) {
        // This timestep's delta rotation to be multiplied by the current rotation
        // after computing it from the gyro sample data.

        if (timestamp != 0f && event != null) {
            val dT = (event.timestamp - timestamp) * NS2S
            // Axis of the rotation sample, not normalized yet.
            var axisX: Float = event.values[0]
            var axisY: Float = event.values[1]
            var axisZ: Float = event.values[2]

            val omegaMagnitude: Float = sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ)

            gyroX.text = "x: $axisX"
            gyroY.text = "y: $axisY"
            gyroZ.text = "z: $axisZ"
            //ManagerSocket.emit("test","test")

        }

    }
}
