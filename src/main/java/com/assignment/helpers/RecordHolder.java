package com.assignment.helpers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecordHolder<T> {
    private Integer totalRecords;
    private T records;
}
