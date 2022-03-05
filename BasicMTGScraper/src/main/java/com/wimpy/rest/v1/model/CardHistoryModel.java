package com.wimpy.rest.v1.model;

import java.math.BigDecimal;
import java.util.Date;

public record CardHistoryModel(BigDecimal price, Date timeCreated) {

}
