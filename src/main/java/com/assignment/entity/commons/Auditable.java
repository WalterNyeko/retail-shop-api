package com.assignment.entity.commons;

import com.assignment.entity.users.User;
import com.assignment.helpers.enums.RecordStatus;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Auditable extends BaseEntity {

    private Date createdDate;
    private Date lastModifiedDate;
    private User createdBy;
    private User lastModifiedBy;
    private RecordStatus recordStatus = RecordStatus.ACTIVE;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    public Date getCreatedDate() {
        return createdDate;
    }


    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_modified_date")
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    @CreatedBy
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "created_by")
    public User getCreatedBy() {
        return createdBy;
    }

    @LastModifiedBy
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "last_modified_by")
    public User getLastModifiedBy() {
        return lastModifiedBy;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "record_status")
    public RecordStatus getRecordStatus() {
        return recordStatus;
    }

}
