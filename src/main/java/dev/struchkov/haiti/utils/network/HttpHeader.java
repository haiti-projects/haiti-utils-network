package dev.struchkov.haiti.utils.network;

import dev.struchkov.haiti.utils.Assert;

/**
 * Утилитарная сущность для {@link HttpParse}. Упрощает сохранения в константы заголовков для запроса.
 *
 * @author upagge 23.12.2020
 */
public class HttpHeader {

    private final String name;
    private final String value;

    private HttpHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public static HttpHeader of(String name, String value) {
        Assert.isNotNull(name, value);
        return new HttpHeader(name, value);
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

}
