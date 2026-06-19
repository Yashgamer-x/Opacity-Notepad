package org.yashgamerx.notepad.service.find;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * {@link TextFinder} implementation backed by {@link java.util.regex}.
 *
 * <p>OCP: New matching strategies (e.g. glob, fuzzy) can be added as additional
 * implementations of {@link TextFinder} without modifying this class.</p>
 */
@Component
public class RegexTextFinder implements TextFinder {

    @Override
    public List<FindResult> findAll(String text, String query, FindOptions options) {
        if (text == null || text.isEmpty() || query == null || query.isBlank()) {
            return Collections.emptyList();
        }

        Pattern pattern = compile(query, options);
        var matcher = pattern.matcher(text);

        List<FindResult> results = new ArrayList<>();
        while (matcher.find()) {
            results.add(new FindResult(matcher.start(), matcher.end()));
        }
        return Collections.unmodifiableList(results);
    }

    private Pattern compile(String query, FindOptions options) {
        String escaped = Pattern.quote(query);
        String wrapped = options.wholeWord() ? "\\b" + escaped + "\\b" : escaped;
        int flags = options.caseSensitive() ? 0 : Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE;
        return Pattern.compile(wrapped, flags);
    }
}
