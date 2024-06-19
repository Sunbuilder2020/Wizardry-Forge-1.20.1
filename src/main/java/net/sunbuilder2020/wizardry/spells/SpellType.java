package net.sunbuilder2020.wizardry.spells;

public enum SpellType {
    NONE(0),
    TARGET(1),
    SELF(2),
    PROJECTILE(3),
    SUMMON(4);

    private final int value;

    SpellType(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}
