package de.hdm_stuttgart.mi.dad.incoming;

public enum ArgumentType {
    PROFILE("--profile", false),

    COLUMN("--column", false),
    TABLE("--table", false),

    EQUAL("--equal", true),
    LIKE("--like", true),
    GREATER("--greater", true),
    RANGE("--range", true),
    REGEX("--regex", true),

    HELP("--help", false);

    public final String argumentString;
    public final boolean isProperty;

    ArgumentType(String argumentString, boolean isProperty){
        this.argumentString = argumentString;
        this.isProperty = isProperty;
    }

    @Override
    public String toString() {
        return argumentString;
    }
}
