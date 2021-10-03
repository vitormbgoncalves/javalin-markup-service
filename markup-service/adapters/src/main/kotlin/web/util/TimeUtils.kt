package web.util

import java.util.Locale

object TimeUtils {
  fun getUptimeInSeconds(uptime: Long): Double =
    getUptime(uptime) / 1000.0

  fun getUptime(startTime: Long): Long =
    System.currentTimeMillis() - startTime

  fun getPrettyUptimeInSeconds(startTime: Long): String =
    format(getUptimeInSeconds(startTime)) + "s"

  fun getPrettyUptimeInMinutes(startTime: Long): String =
    format(getUptimeInSeconds(startTime) / 60) + "min"

  fun format(time: Double): String =
    String.format(Locale.US, "%.2f", time)
}
