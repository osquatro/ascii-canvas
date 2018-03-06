package org.creditsuisse.common;

public class TypeParser {

    public Integer parseInteger(String param) {
        return Integer.valueOf(param);
    }

    public Character parseCharacter(String param) {
        return Character.valueOf(param.charAt(0));
    }

}
