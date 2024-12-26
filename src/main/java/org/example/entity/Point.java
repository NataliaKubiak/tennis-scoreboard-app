package org.example.entity;

import jakarta.persistence.criteria.CriteriaBuilder;

public enum Point {
    LOVE(0),
    FIFTEEN(15),
    THIRTY(30),
    FORTY(40),
    DEUCE(null),
    ADVANTAGE(null);

    private final Integer value;

    Point(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Point next() {
        switch (this) {
            case LOVE: return FIFTEEN;
            case FIFTEEN: return THIRTY;
            case THIRTY: return FORTY;
            case FORTY: return DEUCE;
            case DEUCE: return ADVANTAGE;
            default: throw new IllegalStateException("No next point after ADVANTAGE");
        }
    }

    public Point previous() {
        switch (this) {
            case ADVANTAGE: return DEUCE;
            case DEUCE: return FORTY;
            default: throw new IllegalStateException("Can't go to the point before FORTY");
        }
    }
}
