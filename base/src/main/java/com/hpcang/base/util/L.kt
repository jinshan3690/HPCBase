package com.hpcang.base.util

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

//Logcat统一管理类
class L private constructor() {

    init {
        /* cannot be instantiated */
        throw UnsupportedOperationException("cannot be instantiated")
    }

    companion object {
        @JvmField
        var TAG = "HPC Log"
        private var className: String? = null
        private var methodName: String? = null
        private var lineNumber = 0
        private var TAG_MAX_LENGTH = 2000
        @JvmField
        var isDebug = true

        /**
         * log格式
         */
        @JvmStatic
        private fun createLog(log: String): String {
            val buffer = StringBuffer()
            buffer.append("START")
            buffer.append(className)
            buffer.append(":")
            buffer.append(methodName)
            buffer.append("(")
            buffer.append(lineNumber)
            buffer.append("lings)END")
            buffer.append(log)
            return buffer.toString()
        }

        private val LINE_SEPARATOR = System.getProperty("line.separator")
        @JvmStatic
        private fun printLine(tag: String, isTop: Boolean) {
            if (isTop) {
                e(
                    tag,
                    "╔═══════════════════════════════════════════════════════════════════════════════════════"
                )
            } else {
                e(
                    tag,
                    "╚═══════════════════════════════════════════════════════════════════════════════════════"
                )
            }
        }

        @JvmStatic
        fun printJson(
            tag: String, msg: String, headString: String
        ) {
            if (!isDebug) return
            var message: String
            message = try {
                if (msg.startsWith("{")) {
                    val jsonObject = JSONObject(msg)
                    jsonObject.toString(4) //最重要的方法，就一行，返回格式化的json字符串，其中的数字4是缩进字符数
                } else if (msg.startsWith("[")) {
                    val jsonArray = JSONArray(msg)
                    jsonArray.toString(4)
                } else {
                    msg
                }
            } catch (e: JSONException) {
                msg
            }
            printLine(tag, true)
            message = headString + LINE_SEPARATOR + message
            val lines =
                message.split(LINE_SEPARATOR!!.toRegex()).toTypedArray()
            for (line in lines) {
                e(tag, "║ $line")
            }
            printLine(tag, false)
        }

        /**
         * 执行对象信息
         */
        private fun getMethodNames(sElements: Array<StackTraceElement>) {
            className = sElements[1].fileName
            methodName = sElements[1].methodName
            lineNumber = sElements[1].lineNumber
        }

        @JvmStatic
        fun i(msg: String) {
            if (!isDebug) return
            // Throwable instance must be created before any methods
            getMethodNames(Throwable().stackTrace)
            var start = 0
            var end = TAG_MAX_LENGTH
            val strLength = msg.length
            for (i in 1..99) {
                if (strLength > end) {
                    Log.i(TAG, "第" + i + "行  " + msg.substring(start, end))
                    start = end
                    end = end + TAG_MAX_LENGTH
                } else {
                    Log.i(
                        TAG,
                        "第" + i + "行  " + createLog(
                            msg.substring(start, strLength)
                        )
                    )
                    break
                }
            }
        }

        @JvmStatic
        fun d(msg: String) {
            if (!isDebug) return
            // Throwable instance must be created before any methods
            getMethodNames(Throwable().stackTrace)
            var start = 0
            var end = TAG_MAX_LENGTH
            val strLength = msg.length
            for (i in 1..99) {
                if (strLength > end) {
                    Log.d(TAG, "第" + i + "行  " + msg.substring(start, end))
                    start = end
                    end = end + TAG_MAX_LENGTH
                } else {
                    Log.d(
                        TAG,
                        "第" + i + "行  " + createLog(
                            msg.substring(start, strLength)
                        )
                    )
                    break
                }
            }
        }

        @JvmStatic
        fun e(msg: String) {
            if (!isDebug) return
            // Throwable instance must be created before any methods
            getMethodNames(Throwable().stackTrace)
            var start = 0
            var end = TAG_MAX_LENGTH
            val strLength = msg.length
            for (i in 1..99) {
                if (strLength > end) {
                    Log.e(TAG, "第" + i + "行  " + msg.substring(start, end))
                    start = end
                    end = end + TAG_MAX_LENGTH
                } else {
                    Log.e(
                        TAG,
                        "第" + i + "行  " + createLog(
                            msg.substring(start, strLength)
                        )
                    )
                    break
                }
            }
        }

        @JvmStatic
        fun v(msg: String) {
            if (!isDebug) return
            // Throwable instance must be created before any methods
            getMethodNames(Throwable().stackTrace)
            var start = 0
            var end = TAG_MAX_LENGTH
            val strLength = msg.length
            for (i in 1..99) {
                if (strLength > end) {
                    Log.v(TAG, "第" + i + "行  " + msg.substring(start, end))
                    start = end
                    end = end + TAG_MAX_LENGTH
                } else {
                    Log.v(
                        TAG,
                        "第" + i + "行  " + createLog(
                            msg.substring(start, strLength)
                        )
                    )
                    break
                }
            }
        }

        @JvmStatic
        fun i(tag: String, msg: String) {
            if (!isDebug) return
            // Throwable instance must be created before any methods
            getMethodNames(Throwable().stackTrace)
            var start = 0
            var end = TAG_MAX_LENGTH
            val strLength = msg.length
            for (i in 1..99) {
                if (strLength > end) {
                    Log.i(tag, "第" + i + "行  " + msg.substring(start, end))
                    start = end
                    end = end + TAG_MAX_LENGTH
                } else {
                    Log.i(
                        tag,
                        "第" + i + "行  " + createLog(
                            msg.substring(start, strLength)
                        )
                    )
                    break
                }
            }
        }

        @JvmStatic
        fun d(tag: String, msg: String) {
            if (!isDebug) return
            // Throwable instance must be created before any methods
            getMethodNames(Throwable().stackTrace)
            var start = 0
            var end = TAG_MAX_LENGTH
            val strLength = msg.length
            for (i in 1..99) {
                if (strLength > end) {
                    Log.d(tag, "第" + i + "行  " + msg.substring(start, end))
                    start = end
                    end = end + TAG_MAX_LENGTH
                } else {
                    Log.d(
                        tag,
                        "第" + i + "行  " + createLog(
                            msg.substring(start, strLength)
                        )
                    )
                    break
                }
            }
        }

        @JvmStatic
        fun e(tag: String, msg: String) {
            if (!isDebug) return
            // Throwable instance must be created before any methods
            getMethodNames(Throwable().stackTrace)
            var start = 0
            var end = TAG_MAX_LENGTH
            val strLength = msg.length
            for (i in 1..99) {
                if (strLength > end) {
                    Log.e(tag, "第" + i + "行  " + msg.substring(start, end))
                    start = end
                    end = end + TAG_MAX_LENGTH
                } else {
                    Log.e(
                        tag,
                        "第" + i + "行  " + createLog(
                            msg.substring(start, strLength)
                        )
                    )
                    break
                }
            }
        }

        @JvmStatic
        fun v(tag: String, msg: String) {
            if (!isDebug) return
            // Throwable instance must be created before any methods
            getMethodNames(Throwable().stackTrace)
            var start = 0
            var end = TAG_MAX_LENGTH
            val strLength = msg.length
            for (i in 1..99) {
                if (strLength > end) {
                    Log.v(tag, "第" + i + "行  " + msg.substring(start, end))
                    start = end
                    end = end + TAG_MAX_LENGTH
                } else {
                    Log.v(
                        tag,
                        "第" + i + "行  " + createLog(
                            msg.substring(start, strLength)
                        )
                    )
                    break
                }
            }
        }
    }
}