package exception.handler.module.config

import jakarta.annotation.PostConstruct
import org.apache.commons.lang3.StringUtils
import org.springframework.core.io.ResourceLoader
import org.springframework.core.io.support.ResourcePatternUtils
import org.springframework.stereotype.Component
import java.io.StringReader
import java.text.MessageFormat
import java.util.*

@Component
class MessageResolver(private val resourceLoader: ResourceLoader) {

    private val messageMap = HashMap<String, Properties>()

    @PostConstruct
    fun init() {
        val resources =
            ResourcePatternUtils
                .getResourcePatternResolver(resourceLoader)
                .getResources("classpath:messages/*.properties")

        resources.forEach {
            val key = StringUtils.substringBetween(it.filename!!, "_", ".")
            messageMap.putIfAbsent(key, Properties())
            messageMap.computeIfPresent(key) { _, p->
                p.load(StringReader(it.getContentAsString(Charsets.UTF_8))); p
            }
        }
    }

    fun getErrorMessage(
        key: String,
        locale: Locale? = null,
        vararg args: String? = arrayOfNulls(1)
    ): String {
        val l = locale ?: Locale("en")
        return MessageFormat.format(messageMap
            .getOrDefault(l.language, messageMap["en"])
            ?.getProperty(key)
            ?: "Internal error.", *args)
    }
}