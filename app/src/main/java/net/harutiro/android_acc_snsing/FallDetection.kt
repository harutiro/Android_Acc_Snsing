package net.harutiro.android_acc_snsing

import kotlin.math.sqrt

/**
 * 転倒検知クラス。
 */
class FallDetection {
    private val normData = mutableListOf<Double>()
    private val threshold: Double = 20.0 // 閾値

    /**
     * 加速度データを追加して転倒を検知します。
     *
     * @param x X軸方向の加速度
     * @param y Y軸方向の加速度
     * @param z Z軸方向の加速度
     * @return 転倒が検知された場合はtrue、そうでない場合はfalse
     */
    fun addAccelerationData(x: Double, y: Double, z: Double): Boolean {
        // ノルムの計算
        val norm = calculateNorm(x, y, z)

        // ノイズ除去処理
        val noiseRemovedNorm = noiseRemoval(norm)

        // ノルムデータの追加
        normData.add(noiseRemovedNorm)

        // 最新のデータを含む5つのノルムデータを使用して転倒を判定
        return if (normData.size >= 5) {
            norm >= threshold
        } else {
            false
        }
    }

    /**
     * ノルムを計算します。
     *
     * @param x X軸方向の加速度
     * @param y Y軸方向の加速度
     * @param z Z軸方向の加速度
     * @return ノルムの値
     */
    private fun calculateNorm(x: Double, y: Double, z: Double): Double {
        return sqrt(x * x + y * y + z * z)
    }

    /**
     * ノイズ除去処理を行います。
     *
     * @param norm ノルムの値
     * @return ノイズ除去されたノルムの値
     */
    private fun noiseRemoval(norm: Double): Double {
        // ノイズ除去処理の実装（過去の5つのデータの平均値を使用）
        return if (normData.size >= 5) {
            var sum = norm
            for (i in normData.size - 4 until normData.size) {
                sum += normData[i]
            }
            sum / 5
        } else {
            norm
        }
    }
}
