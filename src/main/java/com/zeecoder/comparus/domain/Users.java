package com.zeecoder.comparus.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Users {
    String id;
    String username;
    String name;
    String surname;
}
