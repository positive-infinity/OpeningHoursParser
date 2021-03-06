package ch.poole.openinghoursparser;
/**
 * Utility methods for ops on OH rules
 * 
 * @author Simon Poole
 *
 *         Copyright (c) 2015 Simon Poole
 *
 *         Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 *         documentation files (the "Software"), to deal in the Software without restriction, including without
 *         limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 *         Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 *         conditions:
 * 
 *         The above copyright notice and this permission notice shall be included in all copies or substantial portions
 *         of the Software.
 *
 *         THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 *         TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 *         THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 *         CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE " OR THE USE OR OTHER
 *         DEALINGS IN THE SOFTWARE.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Util {

    private Util() {
        // dummy private constructor
    }

    /**
     * find rules that only differs in the days and times objects and can be merged for display puposes
     * 
     * @param rules rules to check
     * @return list of list of rules that can be merged
     */
    public static List<ArrayList<Rule>> getMergeableRules(List<Rule> rules) {

        List<ArrayList<Rule>> result = new ArrayList<ArrayList<Rule>>();

        ArrayList<Rule> copy = new ArrayList<Rule>(rules); // shallow copy for bookkeeping

        while (!copy.isEmpty()) {
            Rule r = copy.get(0);
            boolean found = false;
            for (ArrayList<Rule> mergeables : result) {
                if (!mergeables.isEmpty() && r.isMergeableWith(mergeables.get(0))) {
                    mergeables.add(r);
                    found = true;
                    break;
                }
            }
            if (!found) {
                ArrayList<Rule> m = new ArrayList<Rule>();
                m.add(r);
                result.add(m);
            }
            copy.remove(0);
        }
        return result;
    }

    /**
     * Generate an OH string from rules
     * 
     * @param rules rules to convert to an opening_hours string
     * @return specification conformant opening_hours string
     */
    public static String rulesToOpeningHoursString(List<Rule> rules) {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Rule r : rules) {
            if (!r.isEmpty()) {
                if (!first) {
                    if (r.isAdditive()) {
                        result.append(", ");
                    } else if (r.isFallBack()) {
                        result.append(" || ");
                    } else {
                        result.append("; ");
                    }
                } else {
                    first = false;
                }
                result.append(r.toString());
            }
        }
        return result.toString();
    }

    /**
     * Generate a debugging output string from rules
     * 
     * @param rules rules to convert to an opening_hours string
     * @return specification debugging opening_hours string
     */
    public static String rulesToOpeningHoursDebugString(List<Rule> rules) {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Rule r : rules) {
            if (!r.isEmpty()) {
                if (!first) {
                    if (r.isAdditive()) {
                        result.append(", ");
                    } else if (r.isFallBack()) {
                        result.append(" || ");
                    } else {
                        result.append("; ");
                    }
                } else {
                    first = false;
                }
                result.append(r.toDebugString());
            }
        }
        return result.toString();
    }

    /**
     * Capitalize a string
     * 
     * @param s string to capitalize
     * @return capitalized string
     */
    public static String capitalize(String s) {
        char[] c = s.toLowerCase(Locale.US).toCharArray();
        if (c.length > 0) {
            c[0] = Character.toUpperCase(c[0]);
            return new String(c);
        }
        return s;
    }

    public static String deWeekDays2En(String s) {
        String l = s.toLowerCase(Locale.US);
        if ("mo".equals(l)) {
            return "Mo";
        } else if ("di".equals(l)) {
            return "Tu";
        } else if ("mi".equals(l)) {
            return "We";
        } else if ("do".equals(l)) {
            return "Th";
        } else if ("fr".equals(l)) {
            return "Fr";
        } else if ("sa".equals(l)) {
            return "Sa";
        }
        return "Su";
    }

    @SuppressWarnings("unchecked")
    /**
     * Copy a list, creating copies of its contents
     * 
     * @param l List to copy
     * @return deep copy of the List or null if it is null
     */
    static <T extends Copy<?>> List<T> copyList(List<T> l) {
        if (l == null) {
            return null;
        }
        List<T> r = new ArrayList<T>(l.size());
        for (T o : l) {
            r.add((T) o.copy());
        }
        return r;
    }
}
