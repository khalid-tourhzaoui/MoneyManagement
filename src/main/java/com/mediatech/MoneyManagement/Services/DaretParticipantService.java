package com.mediatech.MoneyManagement.Services;


import java.util.List;


import com.mediatech.MoneyManagement.Models.DaretParticipant;

public interface DaretParticipantService {
    List<DaretParticipant> getAllDaretParticipants();
    DaretParticipant getDaretParticipantById(Long id);
    // Add more methods as needed
    void addParticipantToDaretOperation(Long daretOperationId, Long userId, String paymentType,float montantPaye);
}
