package com.khandras.adapter.bot.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import resources.proto.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Service
public class BotAdapter {
    private final ManagedChannel channel = ManagedChannelBuilder
            .forAddress("localhost", 9090)
            .usePlaintext().build();

    public List<UserInfo> getUserInfoById(String userId) {

        SaveInfoServiceGrpc.SaveInfoServiceBlockingStub infoServiceBlockingStub = SaveInfoServiceGrpc.newBlockingStub(channel);

        var request = GetInfoRequest.newBuilder().setUserId(userId).build();

        var response = infoServiceBlockingStub.getUserInfo(request);

        return response.getInfoList();
    }
    public List<GetMessagesResponse> startBidirectionalStreaming(String userId) {
        SaveInfoServiceGrpc.SaveInfoServiceStub infoServiceStub = SaveInfoServiceGrpc.newStub(channel);
        List<GetMessagesResponse> responseMessages = new CopyOnWriteArrayList<>();

        var responseObserver = new StreamObserver<GetMessagesResponse>() {
            @Override
            public void onNext(GetMessagesResponse response) {
                log.info("RESPONSE infoList size: {}, info list:{}", response.getInfoList().size(), response.getInfoList().stream().map(Message::getMessageId).toList());
                responseMessages.add(response);
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("some error in response", throwable);
            }

            @Override
            public void onCompleted() {
                log.info("Finished bidirectionalStreamingGetListsStockQuotes");
            }
        };

        StreamObserver<GetInfoRequest> requestStreamObserver = infoServiceStub.getUserMessagesBidirectional(responseObserver);

        try {
            var requests = List.of(GetInfoRequest.newBuilder().setUserId(userId).build());
            for (GetInfoRequest request : requests) {
                log.info("REQUEST for user: {}", request.getUserId());
                requestStreamObserver.onNext(request);
            }
        } catch (RuntimeException e) {
            requestStreamObserver.onError(e);
            throw e;
        }

        requestStreamObserver.onCompleted();
        while (responseMessages.size() < 5) {
            System.out.print("wait all responses\t");
        }
        return responseMessages;
    }
}
