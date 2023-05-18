package com.korea.triplocation.domain.travel.entity;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TravelRoutes {
    private int routeId;
    private int travelId;
    private int scheduleId;

}
