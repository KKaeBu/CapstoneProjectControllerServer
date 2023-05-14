package com.server.controlserver.service;

import com.server.controlserver.domain.Ping;
import com.server.controlserver.domain.RoadMap;
import com.server.controlserver.dto.ActivityRequestDto;
import com.server.controlserver.dto.PingRquestDto;
import com.server.controlserver.repository.PingRepository;
import com.server.controlserver.repository.WalkRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Transactional
public class WalkService {

    private WalkRepository walkRepository;
    private PingRepository pingRepository;

    @Autowired
    public WalkService(WalkRepository walkRepository, PingRepository pingRepository) {
        this.walkRepository = walkRepository;
        this.pingRepository = pingRepository;
    }

    public RoadMap endOfWalk(ActivityRequestDto activityRequestDto, ConcurrentHashMap<String, List<PingRquestDto>> pingList) {
        List<Ping> pl = new ArrayList<>();
        RoadMap roadmap = null;

        for (String key : pingList.keySet()) {
            List<PingRquestDto> dataList = pingList.get(key);

            // 데이터베이스에 저장하는 코드 추가
            if(dataList != null){
                System.out.println("pingList: " + dataList);
                System.out.println("pingListType: " + dataList.getClass().getName());
                for (PingRquestDto pingReq : dataList){
                    Ping ping = pingReq.toEntity();
                    pl.add(ping);
//                    pingRepository.save(ping);
                }
                System.out.println("pl: " + pl);
                roadmap = pingRepository.save(pl);
            }

            // 저장이 완료되면, ConcurrentHashMap에서 해당 데이터를 삭제
            pingList.remove(key);
        }

        return roadmap;
    }
}
