package me.randomgamingdev.randomgamingorigins.core.types;

public enum PersistentKey {
    Summoner("summoner"),
    SummonedAt("summonedAt");

    public String strVal;

    PersistentKey(String strVal) {
        this.strVal = strVal;
    }
}
