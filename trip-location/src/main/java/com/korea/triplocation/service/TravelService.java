package com.korea.triplocation.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.korea.triplocation.api.dto.request.LocationReqDto;
import com.korea.triplocation.api.dto.request.PartyDataReqDto;
import com.korea.triplocation.api.dto.request.TravelPlanReqDto;
import com.korea.triplocation.api.dto.request.TravelUpdateReqDto;
import com.korea.triplocation.api.dto.response.MyTravelInfoRespDto;
import com.korea.triplocation.api.dto.response.RegionRespDto;
import com.korea.triplocation.domain.travel.entity.Location;
import com.korea.triplocation.domain.travel.entity.MainImage;
import com.korea.triplocation.domain.travel.entity.Region;
import com.korea.triplocation.domain.travel.entity.Schedule;
import com.korea.triplocation.domain.travel.entity.Travels;
import com.korea.triplocation.repository.TravelRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TravelService {
    private final TravelRepository travelRepository;

    public void travelSave(List<TravelPlanReqDto> travels) {
        if (travels != null && !travels.isEmpty()) {
            String travelName = UUID.randomUUID().toString();
            Integer travelId = null;

            for (TravelPlanReqDto dto : travels) {
                if (dto != null && dto.getLocation() != null && !dto.getPartyData().isEmpty()) {
                    List<LocationReqDto> locations = dto.getLocation();
                    List<PartyDataReqDto> partyData = dto.getPartyData();

                    for (int i = 0; i < locations.size(); i++) {
                        LocationReqDto locationReqDto = locations.get(i);
                        for (PartyDataReqDto partyDataReqDto : partyData) {
                            System.out.println(partyDataReqDto.getUserId());

                            if (i == 0 && travelId == null) {
                                // Save the travel with the first location and first participant and get the travel ID
                                travelId = travelRepository.callInsertTravelData(
                                        travelName,
                                        locationReqDto.getAddr(),
                                        locationReqDto.getLat(),
                                        locationReqDto.getLng(),
                                        partyDataReqDto.getUserId(),
                                        dto.getDate() // Visit date
                                );
                            } else {
                                // Save the other locations with the same travel ID and all participants
                                travelRepository.callInsertTravelData(
                                        null,
                                        locationReqDto.getAddr(),
                                        locationReqDto.getLat(),
                                        locationReqDto.getLng(),
                                        partyDataReqDto.getUserId(),
                                        dto.getDate() // Visit date
                                );
                            }
                        }
                    }
                }
            }

        }
    }
    
    private String convertFilePathToUrl(String tempName) {
  		return "http://localhost:8080/image/region/" + tempName;
  	}
    
    public List<Travels> findTravelByUser(int userId) {
    	if(userId == 0) {
    		return null;
    	}
    	List<Travels> travelsList = travelRepository.findTravelAllByUser(userId);
    	
    	for (Travels travel : travelsList) {
    	    List<Region> regions = travel.getRegions();
    	    for (Region region : regions) {
    	        MainImage mainImage = null;
    	        String imgUrl = null;

    	        if (region.getRegionImgId() != -1) {
    	            mainImage = travelRepository.getMainImgById(region.getRegionImgId());
    	            if (mainImage != null) {
    	                imgUrl = convertFilePathToUrl(mainImage.getTempName());
    	                region.setRegionImgUrl(imgUrl);
    	            }
    	        } else {
    	            imgUrl = convertFilePathToUrl("default.png");
    	            region.setRegionImgUrl(imgUrl);
    	        }
    	    }
    	}
    	return travelsList;
    }
    
    public RegionRespDto findRegionByTravelName(String travelName) {
    	Region region = null;
    	MainImage mainImage = null;
        String imgUrl = null;
        
    	String[] words = travelName.split(" ");
    	for (String word : words) {
    		String comparisonWord = word.substring(0,2);
    		region = travelRepository.findRegionByTravelName(comparisonWord);
    		if(region != null) {
    			break;
    		}
    	}
    	
        if (region == null) {
            return null;
        }
        
        if (region.getRegionImgId() != -1) {
            mainImage = travelRepository.getMainImgById(region.getRegionImgId());
            if (mainImage != null) {
                imgUrl = convertFilePathToUrl(mainImage.getTempName());
            }
        } else {
            imgUrl = convertFilePathToUrl("default.png");
        }

        return RegionRespDto.builder()
                .regionId(region.getRegionId())
                .regionName(region.getRegionName())
                .regionEngName(region.getRegionEngName())
                .regionDescription(region.getRegionDescription())
                .regionImgUrl(imgUrl)
                .build();
    }


    
    public MyTravelInfoRespDto findTravelInfoByTravelId(int userId, int travelId) {
        Travels travelByTravelId = travelRepository.findTravelByTravelIdAndUserId(userId, travelId);

        return MyTravelInfoRespDto.builder()
                .schedules(travelByTravelId.getSchedules())
                .build();
    }



    public void updateTravel(int travelId, TravelUpdateReqDto travelUpdateReqDto) {
        Travels travels = travelRepository.findTravelByTravelId(travelId);
        if(travels != null) {
            for(Schedule schedule : travelUpdateReqDto.getSchedules()) {
                for (Location location: schedule.getLocations()) {
                    System.out.println(location.getAddr());
                    travelRepository.updateTravelData(location.getLocationId(), location.getAddr(), location.getLat(), location.getLng());
                }
            };
        };

    }
}
