package Hibernate_classes;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class HistoryPK implements Serializable {
    private long userid;
    private long mid;

    @Column(name = "USERID", nullable = false, precision = 0)
    @Id
    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    @Column(name = "MID", nullable = false, precision = 0)
    @Id
    public long getMid() {
        return mid;
    }

    public void setMid(long mid) {
        this.mid = mid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoryPK historyPK = (HistoryPK) o;
        return userid == historyPK.userid &&
                mid == historyPK.mid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userid, mid);
    }
}
