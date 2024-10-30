package com.zeecoder.comparus.configuration.properties_set;

public record DataSet(
    String name,
    String strategy,
    String url,
    String table,
    String user,
    String password,
    TableSet mapping
){}
