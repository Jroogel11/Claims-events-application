package com.JroogelProyects.claims_event_sourcing.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.JroogelProyects.claims_event_sourcing.dto.ClaimEventMessage;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClaimEventProducer {
    
    KafkaTemplate<String, ClaimEventMessage> template;

    @Value("${kafka.topics.claim-events}")
    private String claimTopic;

    @Autowired
    public ClaimEventProducer(KafkaTemplate<String, ClaimEventMessage> template){
        this.template = template;
    }

    public void sendClaimEvent(ClaimEventMessage event){
        template.send(claimTopic,event.getEventId().toString(),event)
        .whenComplete((result,exception) ->{
            if(exception != null){
                log.error("Error sending the {} event" , event.getEventId().toString());
            }else{
                log.info("Event {} sended" , event.getEventId().toString());
            }
        });
    }
}
