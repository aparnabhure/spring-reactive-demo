/*
 *   Copyright © 2022 Broadcom. All rights reserved. The term “Broadcom” refers to Broadcom Inc. and/or its subsidiaries.
 *   This software and all information contained therein is confidential and proprietary and shall not be duplicated, used, disclosed or disseminated in any way except as authorized by the applicable license agreement, without the express written permission of Broadcom. All authorized reproductions must be marked with this language.
 *
 *   EXCEPT AS SET FORTH IN THE APPLICABLE LICENSE AGREEMENT, TO THE EXTENT PERMITTED BY APPLICABLE LAW OR AS AGREED BY BROADCOM IN ITS APPLICABLE LICENSE AGREEMENT, BROADCOM PROVIDES THIS DOCUMENTATION “AS IS” WITHOUT WARRANTY OF ANY KIND, INCLUDING WITHOUT LIMITATION, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR NONINFRINGEMENT. IN NO EVENT WILL BROADCOM BE LIABLE TO THE END USER OR ANY THIRD PARTY FOR ANY LOSS OR DAMAGE, DIRECT OR INDIRECT, FROM THE USE OF THIS DOCUMENTATION, INCLUDING WITHOUT LIMITATION, LOST PROFITS, LOST INVESTMENT, BUSINESS INTERRUPTION, GOODWILL, OR LOST DATA, EVEN IF BROADCOM IS EXPRESSLY ADVISED IN ADVANCE OF THE POSSIBILITY OF SUCH LOSS OR DAMAGE.
 */

package com.example.springreactivedemo.service;

import com.example.springreactivedemo.domain.ActionInstanceModel;
import com.example.springreactivedemo.domain.EntityInfo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Component
public class AssetGroupResolver {
    public void resolveAndSave(ActionInstanceModel actionInstanceModel, EntityInfo groupEntity){
        Map<String, Object> attributes = groupEntity.getAdditionalAttributes();
        if(MapUtils.isEmpty(attributes)){
            attributes = new HashMap<>();
        }
        int totalCount = MapUtils.getInteger(attributes, "device_count", 0);
        attributes.remove("device_count");
        groupEntity.setAdditionalAttributes(attributes);

        AtomicInteger offset = new AtomicInteger(0);
        while (offset.get() < totalCount){
            System.out.println("*********** Loop Offset " + offset.get() +" Group "+ groupEntity.getEntityId());
            resolveWithRetry(actionInstanceModel, groupEntity, offset);
        }
        System.out.println("********** Loop END + Group "+groupEntity.getEntityId());
    }

    private void resolveWithRetry(ActionInstanceModel actionInstanceModel, EntityInfo groupEntity, AtomicInteger offset){
        getDevices(actionInstanceModel, groupEntity, offset);
    }

    void getDevices(ActionInstanceModel actionInstanceModel, EntityInfo groupEntityInfo, AtomicInteger offset) {
        List<EntityInfo> deviceList = new ArrayList<>();
        Map<String, Object> groupAdditionalAttributes = groupEntityInfo.getAdditionalAttributes();
        for(int i=0; i<1000; i++){
            EntityInfo entity = new EntityInfo();
            entity.setEntityId(UUID.randomUUID().toString());
            Map<String, Object> attributes = new HashMap<>(groupAdditionalAttributes);
            attributes.put("device_group_id", groupEntityInfo.getEntityId());
            attributes.put("name", entity.getEntityId());
            entity.setAdditionalAttributes(attributes);
            deviceList.add(entity);
        }

        offset.addAndGet(deviceList.size());
        System.out.println("********** Offset " + offset.get() +" Group "+ groupEntityInfo.getEntityId());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException var3) {
            System.out.println("*********** Thread interrupted");
        }
    }
}
