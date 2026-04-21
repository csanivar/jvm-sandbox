package org.example.problems

class SemanticVersionCompare {

    // 1. data class: The Kotlin equivalent of a Java Record
    data class Version(
        val value: String,
        val major: Int,
        val minor: Int,
        val patch: Int
    ) : Comparable<Version> {

        // 2. compareValuesBy: Kotlin's elegant, built-in way to chain comparisons
        override fun compareTo(other: Version): Int =
            compareValuesBy(this, other, { it.major }, { it.minor }, { it.patch })

        companion object {
            // 3. Raw strings (""") mean no double-escaping backslashes
            private val REGEX = """v?(\d+)\.(\d+)(?:\.(\d+))?""".toRegex()

            fun parse(value: String): Version {
                // 4. Elvis operator (?:) for fast null-checking
                val match = REGEX.find(value)
                    ?: throw IllegalArgumentException("Invalid version format: $value")

                // 5. Destructuring: Missing optional groups automatically return empty strings ("")
                val (major, minor, patch) = match.destructured

                return Version(
                    value = value,
                    major = major.toInt(),
                    minor = minor.toInt(),
                    // 6. toIntOrNull() safely turns an empty string "" into null, which defaults to 0
                    patch = patch.toIntOrNull() ?: 0
                )
            }
        }
    }

    fun test() {
        compareAndPrint(Version.parse("0.0.0"), Version.parse("0.1.0")) // Smaller
        compareAndPrint(Version.parse("0.0.0"), Version.parse("0.0.0")) // Equal
        compareAndPrint(Version.parse("1.0"), Version.parse("1.0.0"))   // Equal
        compareAndPrint(Version.parse("1.0.1"), Version.parse("2.0.0")) // Smaller
        compareAndPrint(Version.parse("2.0.1"), Version.parse("2.0.0")) // Greater
        compareAndPrint(Version.parse("2.0.1"), Version.parse("2.0.0-alpha")) // Greater
    }

    private fun compareAndPrint(v1: Version, v2: Version) {
        // 7. String interpolation simplifies printing heavily
        println("v1: ${v1.value}, v2: ${v2.value}, compareVal: ${compareToString(v1.compareTo(v2))}")
    }

    // 8. Single-expression function using 'when'
    private fun compareToString(v: Int): String = when {
        v > 0 -> "Greater"
        v < 0 -> "Smaller"
        else -> "Equal"
    }
}