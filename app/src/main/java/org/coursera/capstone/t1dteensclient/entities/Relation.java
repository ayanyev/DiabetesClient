package org.coursera.capstone.t1dteensclient.entities;

import org.coursera.capstone.t1dteensclient.entities.enums.RelationStatus;

import java.util.Date;

public class Relation {

    private long rel_id;
    private long subscriber;
    private long subscription;
    private RelationStatus status;
    private Date timestamp;

    public Relation() {
    }

    public Relation(long subscriber, long subscription) {
        this.subscriber = subscriber;
        this.subscription = subscription;
        this.status = RelationStatus.PENDING;
    }

    public long getSubscription() {
        return subscription;
    }

    public void setSubscription(long subscription) {
        this.subscription = subscription;
    }

    public long getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(long subscriber) {
        this.subscriber = subscriber;
    }

    public long getRel_id() {
        return rel_id;
    }

    public void setRel_id(long rel_id) {
        this.rel_id = rel_id;
    }

    public RelationStatus getStatus() {
        return status;
    }

    public void setStatus(RelationStatus status) {
        this.status = status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    /*    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Relation relation = (Relation) o;
        return Objects.equals(subscriber, relation.subscriber) &&
                Objects.equals(subscription, relation.subscription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subscriber, subscription);
    }*/
}
