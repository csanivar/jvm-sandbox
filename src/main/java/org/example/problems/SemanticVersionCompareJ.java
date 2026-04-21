package org.example.problems;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SemanticVersionCompareJ {

    private static final class Version implements Comparable<Version> {
        String val;
        private int major = 0;
        private int minor = 0;
        private int patch = 0;
        private static final Pattern SEMVER_PATTERN = Pattern.compile(
                "v?(?<major>\\d+)\\.(?<minor>\\d+)(?:\\.(?<patch>\\d+))?"
        );

        public Version(String val) {
            this.val = val;
            Matcher matcher = SEMVER_PATTERN.matcher(val);

            if (!matcher.find()) {
                // error & skip. will default to 0.0.0
                return;
            }

            major = Integer.parseInt(matcher.group("major"));
            minor = Integer.parseInt(matcher.group("minor"));
            patch = Integer.parseInt(matcher.group("patch") == null ? "0" : matcher.group("patch"));
        }

        @Override
        public int compareTo(Version that) {
            int majorDiff = Integer.compare(this.major, that.major);
            if (majorDiff != 0) {
                return majorDiff;
            }

            int minorDiff = Integer.compare(this.minor, that.minor);
            if (minorDiff != 0) {
                return minorDiff;
            }

            return Integer.compare(this.patch, that.patch);
        }
    }

    public void test() {
        compareAndPrint(new Version("0.0.0"), new Version("0.1.0")); // Smaller
        compareAndPrint(new Version("0.0.0"), new Version("0.0.0")); // Equal
        compareAndPrint(new Version("1.0"), new Version("1.0.0")); // Equal
        compareAndPrint(new Version("1.0.1"), new Version("2.0.0")); // Smaller
        compareAndPrint(new Version("2.0.1"), new Version("2.0.0")); // Greater
        compareAndPrint(new Version("2.0.1"), new Version("2.0.0-alpha")); // Greater
    }

    private void compareAndPrint(Version v1, Version v2) {
        System.out.printf(
                "v1: %s, v2: %s, compareVal: %s%n", v1.val,
                v2.val,
                compareToString(v1.compareTo(v2))
        );
    }

    private String compareToString(int v) {
        return switch (v) {
            case 1 -> "Greater";
            case -1 -> "Smaller";
            default -> "Equal";
        };
    }
}
