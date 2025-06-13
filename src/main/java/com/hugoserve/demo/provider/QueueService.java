package com.hugoserve.demo.provider;

import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;
import com.hugoserve.demo.proto.QueueMessage;
import com.hugoserve.demo.proto.TransactionRequest;
import com.hugoserve.demo.service.TransactionService;
import com.hugoserve.demo.utils.ProtoMessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class QueueService {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueueService.class);
    private final QueueProvider queueProvider;
    private ProtoMessageConverter messageConverter;
    private TransactionService transactionService;

    public QueueService(QueueProvider queueProvider, ProtoMessageConverter protoMessageConverter, TransactionService transactionService) {
        this.queueProvider = queueProvider;
        this.messageConverter = protoMessageConverter;
        this.transactionService = transactionService;
    }


    public void pollMessages() {
        final List<software.amazon.awssdk.services.sqs.model.Message> messages = this.queueProvider.poll();
        for (software.amazon.awssdk.services.sqs.model.Message message : messages) {
            processMessage(message);
        }
    }


    public boolean processMessage(QueueMessage queueMessage) {

        if (TransactionRequest.class.toString().equals(queueMessage.getType())) {
            TransactionRequest.Builder builder = TransactionRequest.newBuilder();
            try {
                messageConverter.getParser().merge(queueMessage.getData(), builder);
            } catch (InvalidProtocolBufferException e) {
                LOGGER.error("Error ocured at parsing message:", e);
                return false;
            }
            TransactionRequest transactionRequest = builder.build();
            return transactionService.doTransaction(transactionRequest);
        }
        return false;
    }

    @Scheduled(fixedRate = 10000)
    public void startpolling() {
        pollMessages();
    }

    private void processMessage(software.amazon.awssdk.services.sqs.model.Message message) {
        String body = message.body();
        QueueMessage.Builder builder = QueueMessage.newBuilder();
        try {
            messageConverter.getParser().merge(body, builder);
        } catch (InvalidProtocolBufferException e) {
            LOGGER.error("Error ocured at parsing message:", e);
            throw new RuntimeException(e);
        }
        QueueMessage queueMessage = builder.build();
        boolean isSuccessful = false;
        try {
            isSuccessful = this.processMessage(queueMessage);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        if (isSuccessful) {
            this.queueProvider.delete(message);
        }
    }

}

