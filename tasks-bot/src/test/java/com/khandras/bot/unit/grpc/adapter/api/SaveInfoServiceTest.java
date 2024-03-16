package com.khandras.bot.unit.grpc.adapter.api;

import com.google.protobuf.ByteString;
import com.khandras.bot.adapter.api.grpc.SaveInfoService;
import com.khandras.bot.app.api.persistance.UserRepository;
import com.khandras.bot.domain.info.SavedInfo;
import com.khandras.bot.domain.info.SavedInfoType;
import com.khandras.bot.domain.user.User;
import io.grpc.internal.testing.StreamRecorder;
import lombok.extern.slf4j.Slf4j;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Testcontainers;
import resources.proto.GetInfoRequest;
import resources.proto.GetInfoResponse;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@ActiveProfiles("test")
@Testcontainers
@SpringBootTest
class SaveInfoServiceTest {
    private static final String TEST_USER_ID = "test_user_id";
    private static final String TEST_RECORD_NAME = "TEST_RECORD_NAME";

    @Autowired
    private SaveInfoService service;
    @Autowired
    private UserRepository userRepository;

    @Rule
    public PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:11.1")
            .withDatabaseName("tasks-bot")
            .withUsername("sa")
            .withPassword("sa")
            .withLogConsumer(new Slf4jLogConsumer(log));

    @Test
    void getUserInfoTestSuccess() {
        prepareDbInfo();

        GetInfoRequest request = GetInfoRequest.newBuilder()
                .setUserId(TEST_USER_ID)
                .build();

        StreamRecorder<GetInfoResponse> responseObserver = StreamRecorder.create();

        service.getUserInfo(request, responseObserver);

        assertNotNull(responseObserver.getValues());
        var responseInfo = responseObserver.getValues().get(0);
        assertEquals(TEST_RECORD_NAME, responseInfo.getInfo(0).getUserInfo(0).getRecordName());
        assertEquals(TEST_USER_ID, responseInfo.getInfo(0).getTelegramId());
        assertEquals(10L, responseInfo.getInfo(0).getSavedInfoCountBytes());
        assertEquals(ByteString.copyFrom("test".getBytes()), responseInfo.getInfo(0).getUserInfo(0).getSavedInfo());
    }

    private void prepareDbInfo() {
        var savedInfo = new SavedInfo()
                .setInfoType(SavedInfoType.TEXT)
                .setRecordName(TEST_RECORD_NAME)
                .setSavedInfo("test".getBytes());

        var userInfo = new User()
                .setSavedInfoList(List.of(savedInfo))
                .setSavedInfoCountBytes(BigInteger.TEN)
                .setTelegramId(TEST_USER_ID);
        savedInfo.setUserInfo(userInfo);

        userRepository.save(userInfo);
    }
}
