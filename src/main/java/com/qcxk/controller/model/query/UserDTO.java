package com.qcxk.controller.model.query;

import com.qcxk.controller.model.page.PageAndPageSize;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserDTO extends PageAndPageSize {
    private String username;
    private String realName;
}
