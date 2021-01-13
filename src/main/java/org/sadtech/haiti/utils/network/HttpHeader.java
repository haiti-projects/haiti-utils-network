package org.sadtech.haiti.utils.network;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

/**
 * Утилитарная сущность для {@link HttpParse}. Упрощает сохранения в константы заголовков для запроса.
 *
 * @author upagge 23.12.2020
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpHeader {

    private final String name;
    private final String value;

    public static HttpHeader of(@NonNull String name, @NonNull String value) {
        return new HttpHeader(name, value);
    }

}
