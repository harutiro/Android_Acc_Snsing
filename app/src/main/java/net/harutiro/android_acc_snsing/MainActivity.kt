package net.harutiro.android_acc_snsing

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text


/**
 * メインのアクティビティクラス。
 */
class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var accSensor: Sensor? = null

    private var otherFileStorage: OtherFileStorage? = null
    private var fallDetection: FallDetection? = null

    /**
     * アクティビティの初期化処理。
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)

        findViewById<Switch>(R.id.sensor_switch).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                otherFileStorage = OtherFileStorage(this, "${DateUtils.getNowDate()}_SensorLog")
                findViewById<TextView>(R.id.filename_text).text = otherFileStorage?.fileName.toString()
                fallDetection = FallDetection()
            } else {
                otherFileStorage = null
                fallDetection = null
            }
        }
    }

    /**
     * センサーの値が変化したときに呼ばれるコールバック関数。
     *
     * @param event センサーイベント
     */
    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_LINEAR_ACCELERATION) {
            val sensorX = event.values[0]
            val sensorY = event.values[1]
            val sensorZ = event.values[2]

            val strTmp = """加速度センサー
                         X: $sensorX
                         Y: $sensorY
                         Z: $sensorZ"""
            findViewById<TextView>(R.id.accSenserText).text = strTmp

            // ファイルに書き込み
            val log: String = "$sensorX,$sensorY,$sensorZ"
            otherFileStorage?.doLog(log)

            // 振られたかどうか判定
            val isShaken = fallDetection?.addAccelerationData(sensorX.toDouble(), sensorY.toDouble(), sensorZ.toDouble())
            findViewById<TextView>(R.id.result_text).text = if (isShaken == true) "振られた" else "振られてない"

            // ログを出力
            Log.d("log", log)
        }
    }

    /**
     * センサーの精度が変化したときに呼ばれるコールバック関数。
     *
     * @param sensor センサーオブジェクト
     * @param accuracy 精度の値
     */
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Do nothing
    }

    /**
     * アクティビティがバックグラウンドに移行したときに呼ばれる関数。
     */
    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    /**
     * アクティビティがフォアグラウンドに移行したときに呼ばれる関数。
     */
    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_UI)
    }
}
