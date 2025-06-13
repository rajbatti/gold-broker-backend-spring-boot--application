package com.hugoserve.demo.provider.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.hugoserve.demo.proto.QueueMessage;
import com.hugoserve.demo.provider.QueueProvider;
import com.hugoserve.demo.utils.ProtoMessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.DeleteMessageResponse;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;
import software.amazon.awssdk.services.sqs.model.SqsException;

@Service
public class SqsQueueProvider implements QueueProvider {
    @Value("${QUEUE_URL}")
    private String queueURL;
    private SqsClient sqsClient;
    private final Logger logger = LoggerFactory.getLogger(SqsQueueProvider.class);
    private final ProtoMessageConverter messageConverter;

    SqsQueueProvider(SqsClient sqsClient, ProtoMessageConverter messageConverter) {
        this.sqsClient = sqsClient;
        this.messageConverter = messageConverter;
    }

    @Override
    public <T extends Message> boolean pushToFifo(T message, String groupId, int delayInSecs) {
        String messageBody = getMessageBody(message);
        try {
            SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                    .queueUrl(queueURL)
                    .messageBody(messageBody)
                    .messageGroupId(groupId)
                    .delaySeconds(delayInSecs)
                    .build();
            logger.info("Sending message to queue with request {} ", sendMessageRequest);
            SendMessageResponse messageResponse = sqsClient.sendMessage(sendMessageRequest);
            return true;
        } catch (SqsException e) {
            logger.error("Error Occur while Sending message: ", e);
        }
        return false;
    }


    @Override
    public List<software.amazon.awssdk.services.sqs.model.Message> poll() {
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueURL)
                .waitTimeSeconds(20)
                .maxNumberOfMessages(10)
                .messageAttributeNames("All")
                .build();

        return sqsClient.receiveMessage(receiveMessageRequest).messages();
    }


    @Override
    public boolean delete(software.amazon.awssdk.services.sqs.model.Message queueMessage) {
        try {
            final DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                    .queueUrl(queueURL)
                    .receiptHandle(queueMessage.receiptHandle())
                    .build();

            DeleteMessageResponse response = this.sqsClient.deleteMessage(deleteMessageRequest);
            logger.info("Response for the delete message request for the receiptHandle {} is {}", queueMessage.receiptHandle(), response);
            boolean isDeleted = response.sdkHttpResponse().statusCode() == 200;
            return isDeleted;
        } catch (Exception e) {
            logger.error("An exception occurred while deleting message from SQS", e);
            return false;
        }
    }

    private <T extends Message> String getMessageBody(T message) {
        String queueMessageData = getMessageAsString(message);
        String queueMessageType = message.getClass().toString();
        return getMessageAsString(QueueMessage.newBuilder().setData(queueMessageData).setType(queueMessageType).build());
    }


    private <T extends Message> String getMessageAsString(T message) {
        String messageBody;
        try {
            messageBody = this.messageConverter.getPrinter().print(message);
        } catch (InvalidProtocolBufferException e) {
            logger.error("Invalid Object {}", message, e);
            throw new RuntimeException("Invalid Proto request");
        }
        return messageBody;
    }
}
