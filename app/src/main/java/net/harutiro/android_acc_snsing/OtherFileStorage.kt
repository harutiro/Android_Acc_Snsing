package net.harutiro.android_acc_snsing

import android.content.Context
import android.os.Environment
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.PrintWriter
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

/**
 * 他のファイルへのログ保存クラス。
 *
 * @param context コンテキスト
 * @param fileName ファイル名
 */
class OtherFileStorage(val context: Context, val fileName: String) {

    val fileAppend: Boolean = true // true=追記, false=上書き
    val extension: String = ".csv"
    val filePath: String =
        context.applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            .toString().plus("/").plus(fileName).plus(extension) //内部ストレージのDocumentのURL
    val baseTime: OffsetDateTime = OffsetDateTime.now()
    val dimension: Int = 3

    init {
        writeText(firstLog(dimension), filePath)
    }

    /**
     * ログを記録します。
     *
     * @param text ログテキスト
     */
    fun doLog(text: String) {
        writeText(
            ChronoUnit.MILLIS.between(baseTime, OffsetDateTime.now()).toString().plus(",").plus(text),
            filePath
        )
    }

    // CSV一行目の出力をする。
    private fun firstLog(dimension: Int): String {
        return when (dimension) {
            1 -> baseTime.toString().plus(",x")
            2 -> baseTime.toString().plus(",x,y")
            3 -> baseTime.toString().plus(",x,y,z")
            else -> {
                var result: String = baseTime.toString()
                for (i in 0 until dimension) result = result.plus(",").plus(i)
                result
            }
        }
    }

    // 外部ストレージにファイル出力をする関数
    private fun writeText(text: String, path: String) {
        val file = FileWriter(path, fileAppend)
        val printWriter = PrintWriter(BufferedWriter(file))
        printWriter.println(text)
        printWriter.close()
    }
}
