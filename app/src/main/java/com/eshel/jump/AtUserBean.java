package com.eshel.jump;

public class AtUserBean{
    public long userId;
    public AtRange range;

    public AtUserBean(long userId, int start, int length) {
        this.userId = userId;
        range = new AtRange();
        range.from = start;
        range.length = length;
    }

    public static class AtRange{
        public int from;
        public int length;

        public int getStart(){
            return from;
        }

        public int getEnd(){
            return getStart() + length;
        }
    }
}