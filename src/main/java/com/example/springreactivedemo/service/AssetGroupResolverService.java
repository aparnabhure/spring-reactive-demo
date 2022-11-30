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
import org.apache.commons.collections4.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class AssetGroupResolverService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetGroupResolverService.class);
    private final AssetGroupResolver assetGroupResolver;

    public void processWithOutThread(ActionInstanceModel actionInstanceModel, List<EntityInfo> deviceGroupEntities){
        Instant startTime = Instant.now();
        System.out.println("*********** START process");

        deviceGroupEntities.forEach(groupEntity->{
            assetGroupResolver.resolveAndSave(actionInstanceModel, groupEntity);
        });

        Instant endTime = Instant.now();
        Duration duration = Duration.between(startTime, endTime);
        System.out.println("************ END Process " + duration.toMinutes());
    }

    public void process(ActionInstanceModel actionInstanceModel, List<EntityInfo> deviceGroupEntities){
        Instant startTime = Instant.now();
        System.out.println("*********** START process");
        List<List<EntityInfo>> partitions = ListUtils.partition(deviceGroupEntities, 100);
        int count = 1;
        for(List<EntityInfo> entityInfos: partitions){
            try {
                System.out.println("************ START Executor "+count);
                AssetGroupExecutor assetGroupExecutor = new AssetGroupExecutor(actionInstanceModel, entityInfos);
                assetGroupExecutor.executeGroups();
                System.out.println("************ END Executor "+count);
                ++count;
            }catch (final InterruptedException ex) {
                LOGGER.error("Interrupted while waiting for tasks to complete.");
                Thread.currentThread().interrupt();
            } catch (Exception e){
                LOGGER.error("Exception while executing groups in batches {}", e.getMessage(), e);
            }
        }
        //Call workflow execution
        Instant endTime = Instant.now();
        Duration duration = Duration.between(startTime, endTime);
        System.out.println("************ END Process " + duration.toSeconds());
    }

    class AssetGroupExecutor {
        private final Deque<EntityInfo> executors = new ArrayDeque<>();
        private final Deque<EntityInfo> deviceGroupEntities;
        private final ActionInstanceModel actionInstanceModel;

        private AssetGroupExecutor(ActionInstanceModel actionInstanceModel, List<EntityInfo> deviceGroupEntities){
            this.deviceGroupEntities = new ArrayDeque<>();
            this.deviceGroupEntities.addAll(deviceGroupEntities);
            this.actionInstanceModel = actionInstanceModel;
        }

        public void executeGroups() throws InterruptedException, ExecutionException {
            while (true) {

                fillExecutorEntityInfos();

                if(deviceGroupEntities.isEmpty() && executors.isEmpty()){
                    break;
                }

                execute(executors);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException var3) {
                    System.out.println("*********** Thread interrupted");
                }
            }
        }

        private void fillExecutorEntityInfos(){
            while (!deviceGroupEntities.isEmpty()){
                executors.push(deviceGroupEntities.pop());
                int maxCapacity = 10;
                if(executors.size() == maxCapacity){
                    break;
                }
            }
        }

        private void execute(final Deque<EntityInfo> executors) throws InterruptedException, ExecutionException{
            ExecutorService executorService = null;

            try {
                executorService = Executors.newFixedThreadPool(executors.size());

                Collection<Callable<EntityInfo>> callables = new ArrayList<>();
                IntStream.rangeClosed(1, executors.size()).forEach(i ->
                    callables.add(createCallable(executors.pop()))
                );

                /* invoke all supplied Callables */
                List<Future<EntityInfo>> taskFutureList = executorService.invokeAll(callables);

                // call get on Futures to retrieve result.
                for (Future<EntityInfo> future : taskFutureList) {
                    EntityInfo entityInfo = future.get();
                    LOGGER.info("EntityInfo {}: migration task completed {}", entityInfo.getEntityId(), future.isDone());
                }
            } finally {
                if(executorService != null) {
                    executorService.shutdown();
                }
            }
        }

        private Callable<EntityInfo> createCallable(final EntityInfo entityInfo){
            return () -> {
                LOGGER.info("Entity{} to  resolve", entityInfo.getEntityId());
                assetGroupResolver.resolveAndSave(actionInstanceModel, entityInfo);
                return entityInfo;
            };
        }
    }
}
