package net.harutiro.android_acc_snsing

import android.icu.text.SimpleDateFormat
import java.util.*

/**
 * 日付関連のユーティリティクラス。
 */
class DateUtils {

    companion object {
        /**
         * 現在の日付を取得します。
         *
         * @return 現在の日付の文字列（"yyyy-MM-dd-HH-mm-ss"形式）
         */
        fun getNowDate(): String {
            val df = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault())
            val date = Date(System.currentTimeMillis())
            return df.format(date)
        }

        /**
         * 文字列を日付に変換します。
         *
         * @param dateString 変換する日付の文字列（"yyyy-MM-dd-HH-mm-ss"形式）
         * @return 変換された日付のミリ秒表現
         */
        fun stringToDate(dateString: String): Long {
            val df = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault())
            val date = df.parse(dateString)
            return date?.time ?: 0L
        }

        /**
         * 現在のタイムスタンプを取得します。
         *
         * @return 現在のタイムスタンプのミリ秒表現
         */
        fun getTimeStamp(): Long {
            return System.currentTimeMillis()
        }
    }
}
