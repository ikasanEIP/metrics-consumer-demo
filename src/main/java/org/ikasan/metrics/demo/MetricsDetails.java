package org.ikasan.metrics.demo;

public class MetricsDetails {
    private int count = 0;
    private int payloadCount;

    public void incrementCount() {
        this.count++;
    }

    public int getCount() {
        return count;
    }

    public int getPayloadCount() {
        return payloadCount;
    }

    public void incrementPayloadCount() {
        this.payloadCount++;
    }



    @Override
    public String toString() {
        return "MetricsDetails{" +
                "count=" + count +
                ", payloadCount=" + payloadCount +
                '}';
    }
}
