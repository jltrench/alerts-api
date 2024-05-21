package com.jlt.security.alert;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlertDao {
    private String type;
    private String message;
    private String coords;
    private String author;
}
