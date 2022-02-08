package com.assignment.dto.commons;

import com.assignment.helpers.enums.RecordStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditableDto {
    private Integer id;
    private Date createdDate;
    private Date lastModifiedDate;
    private Integer createdBy;
    private Integer lastModifiedBy;
    private RecordStatus recordStatus;
}
