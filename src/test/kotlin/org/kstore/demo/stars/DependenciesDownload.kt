package org.kstore.demo.stars

import org.junit.jupiter.api.*
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels
import java.security.cert.X509Certificate
import javax.net.ssl.*


@Disabled
class DependenciesDownload {

    @Test
    fun download() {
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate>? = null
            override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}
            override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
        })

        // Install the all-trusting trust manager
        val sc = SSLContext.getInstance("SSL")
        sc.init(null, trustAllCerts, java.security.SecureRandom())
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)

        // Create all-trusting host name verifier
        val allHostsValid = HostnameVerifier { _, _ -> true }

        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid)

        val dependency = "com.fasterxml.jackson.module:jackson-module-kotlin:2.9.4.1"
        val parts = dependency.split(":")

        val groupId = parts[0]
        val artifactId = parts[1]
        val version = parts[2]

        val url = "https://repo1.maven.org/maven2/${groupId.replace('.', '/')}/$artifactId/$version/$artifactId-$version.jar"

        val website = URL(url)
        val rbc = Channels.newChannel(website.openStream())
        val fos = FileOutputStream("$artifactId-$version.jar")
        fos.channel.transferFrom(rbc, 0, java.lang.Long.MAX_VALUE)

        Runtime
                .getRuntime()
                .exec("C:\\apps\\apache-maven-3.2.5\\bin\\mvn install:install-file -Dfile=\"$artifactId-$version.jar\" -DgroupId=$groupId -DartifactId=$artifactId -Dversion=$version -Dpackaging=jar -DgeneratePom=true")
                .waitFor()
    }
}
