package com.khandras.bot.adapter.api.grpc;

import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import com.khandras.bot.app.api.history.HistoryService;
import com.khandras.bot.domain.info.SavedInfo;
import com.khandras.bot.domain.user.User;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;
import org.springframework.transaction.annotation.Transactional;
import resources.proto.*;

import java.time.ZoneOffset;
import java.util.stream.IntStream;

@Slf4j
@GRpcService
@RequiredArgsConstructor
public class SaveInfoService extends SaveInfoServiceGrpc.SaveInfoServiceImplBase {
    private final HistoryService historyService;

    @Override
    public void getUserMessages(GetInfoRequest request, StreamObserver<GetMessagesResponse> responseObserver) {
        log.info("received request for user messages [{}]", request.getUserId());

        var response = GetMessagesResponse.newBuilder();

        historyService.findUserMessages(request.getUserId()).stream()
                .map(messageLog -> Message.newBuilder()
                        .setMessageId(messageLog.getMessageId().toString())
                        .setMessageFull(messageLog.getMessageFull().toString())
                        .setSaveDateTime(Timestamp.newBuilder()
                                .setSeconds(messageLog.getSaveDateTime().toEpochSecond(ZoneOffset.ofHours(3))).build())
                        .setMessageText(messageLog.getMessageText() == null ? "" : messageLog.getMessageText()))
                .forEach(response::addInfo);

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<GetInfoRequest> getUserMessagesBidirectional(StreamObserver<GetMessagesResponse> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(GetInfoRequest getInfoRequest) {
                log.info("Income request for user messages {}", getInfoRequest.getUserId());

                IntStream.range(0, 5).forEach(pageNum -> {
                    var response = GetMessagesResponse.newBuilder();
                    historyService.findLastUserMessagesByPage(getInfoRequest.getUserId(), pageNum, 3).stream()
                            .map(messageLog -> Message.newBuilder()
                                    .setMessageId(messageLog.getMessageId().toString())
                                    .setMessageFull(messageLog.getMessageFull().toString())
                                    .setSaveDateTime(Timestamp.newBuilder()
                                            .setSeconds(messageLog.getSaveDateTime().toEpochSecond(ZoneOffset.ofHours(3))).build())
                                    .setMessageText(messageLog.getMessageText() == null ? "" : messageLog.getMessageText()))
                            .forEach(response::addInfo);

                    responseObserver.onNext(response.build());
                });
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("Occurred an error in preparing response for bidirectional streaming", throwable);
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    @Transactional
    public void getUserInfo(GetInfoRequest request, StreamObserver<GetInfoResponse> responseObserver) {
        log.info("received request for user info [{}]", request.getUserId());

        var response = historyService.findUserInfo(request.getUserId())
                .map(this::prepareResponse)
                .orElse(GetInfoResponse.newBuilder().build());

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private GetInfoResponse prepareResponse(User userInfo) {
        var savedInfoListDto = userInfo.getSavedInfoList().stream()
                .map(this::prepareSavedInfo).toList();
        var userInfoDto = resources.proto.UserInfo.newBuilder()
                .setSavedInfoCountBytes(userInfo.getSavedInfoCountBytes().longValue())
                .setTelegramId(userInfo.getTelegramId())
                .addAllUserInfo(savedInfoListDto)
                .build();

        return GetInfoResponse.newBuilder().addInfo(userInfoDto).build();
    }

    private resources.proto.SavedInfo prepareSavedInfo(SavedInfo savedInfo) {
        var savedInfoRs = resources.proto.SavedInfo.newBuilder()
                .setSavedInfo(ByteString.copyFrom(savedInfo.getSavedInfo()))
                .setInfoType(SavedInfoType.forNumber(savedInfo.getInfoType().ordinal()))
                .setRecordName(savedInfo.getRecordName())
                .setInfoId(savedInfo.getInfoId().toString());

        return savedInfo.getUpdateDateTime() == null ? savedInfoRs.build() :
                savedInfoRs.setSaveDateTime(savedInfo.getUpdateDateTime().toString()).build();
    }
}
