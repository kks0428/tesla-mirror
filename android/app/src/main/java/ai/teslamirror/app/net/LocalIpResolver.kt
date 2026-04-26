package ai.teslamirror.app.net

import java.net.NetworkInterface

object LocalIpResolver {
    fun guessHotspotIp(): String {
        val candidates = NetworkInterface.getNetworkInterfaces()?.toList().orEmpty()
            .flatMap { it.inetAddresses.toList() }
            .map { it.hostAddress.orEmpty() }
            .filter { it.isNotBlank() && !it.contains(':') && it != "127.0.0.1" }

        return candidates.firstOrNull() ?: "phone-ip"
    }
}
