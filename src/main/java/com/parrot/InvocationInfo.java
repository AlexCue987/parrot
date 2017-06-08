package com.parrot;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.SortedMap;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class InvocationInfo implements Comparable<InvocationInfo> {
    @NonNull
    private final String methodName;
    @NonNull
    private final String path;
    @NonNull
    private final SortedMap<String, String> queryParameters;

    @Override
    public int compareTo(InvocationInfo o) {
        int result = methodName.compareTo(o.getMethodName());
        if(result != 0){
            return result;
        }
        String mapAsSortedKeyValuePairs = printMapAsSortedKeyValuePairs(queryParameters);
        String anotherMapAsSortedKeyValuePairs = printMapAsSortedKeyValuePairs(o.getQueryParameters());
        return mapAsSortedKeyValuePairs.
                compareTo(anotherMapAsSortedKeyValuePairs);
    }

    static String printMapAsSortedKeyValuePairs(SortedMap<String, String> queryParameters){
        return queryParameters.keySet().stream().
                sorted().
                map(key -> String.format("%s=%s", key, queryParameters.get(key))).
                collect(Collectors.joining(";"));
    }
}
