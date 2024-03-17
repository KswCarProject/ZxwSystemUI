package com.android.systemui.dreams.complication;

public class ComplicationId {
    public int mId;

    public static class Factory {
        public int mNextId;

        public ComplicationId getNextId() {
            int i = this.mNextId;
            this.mNextId = i + 1;
            return new ComplicationId(i);
        }
    }

    public ComplicationId(int i) {
        this.mId = i;
    }

    public String toString() {
        return "ComplicationId{mId=" + this.mId + "}";
    }
}
