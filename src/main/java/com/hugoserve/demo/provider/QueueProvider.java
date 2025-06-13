package com.hugoserve.demo.provider;

import java.util.List;

import com.google.protobuf.Message;

public interface QueueProvider {

    <T extends Message> boolean pushToFifo(T message, String groupId, int delayInSecs);

    List<software.amazon.awssdk.services.sqs.model.Message> poll();

     boolean delete(software.amazon.awssdk.services.sqs.model.Message queueMessage);
}
