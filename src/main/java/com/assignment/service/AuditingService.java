package com.assignment.service;


import com.assignment.entity.commons.Auditable;
import com.assignment.entity.users.User;
import com.assignment.helpers.UserContext;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuditingService {

    public <T extends Auditable> void stamp(T entity) {
        User user = UserContext.getLoggedInUser();
        Date now = new Date();
        entity.setLastModifiedBy(user);
        entity.setLastModifiedDate(now);
        if (entity.getCreatedBy() == null) {
            entity.setCreatedBy(user);
        }
        if (entity.getCreatedDate() == null) {
            entity.setCreatedDate(now);
        }
    }
}

