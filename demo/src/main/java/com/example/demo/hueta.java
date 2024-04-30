package com.example.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import okhttp3.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

@Service
public class hueta {
    @PostConstruct
    public void post() throws IOException {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("user"));
    }

//        OkHttpClient client = new OkHttpClient();
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        List<String> ss = new ArrayList<>();
//        String s = objectMapper.writeValueAsString(new DataToChangeTariff("79218476904", 12L));
//        ss.add(s);
//
//
//        RequestBody body = RequestBody.create(MediaType.parse("application/json"), s);
//
////        Request request = new Request.Builder()
////                .url("http://localhost:2004/admin")
////                .get()
////                .addHeader("Authorization",getBasicAuthenticationHeader("admin", "admin"))
////                .build();
////
////        System.out.println(request);
////
////        Response response = client.newCall(request).execute();
////        System.out.println(response);
//        Request request1 = new Request.Builder()
//
//
//                .url("http://localhost:2004/admin/change-tariff")
//                .post(body)
//                .addHeader("Authorization",getBasicAuthenticationHeader("admin", "admin"))
//                .build();
//
////        System.out.println(request1);
////        System.out.println(request1.body().toString());
//
//        Response response1 = client.newCall(request1).execute();
//
//        System.out.println(response1.code() + "; " + response1.body().string());
//
//    }
//
//    private String getBasicAuthenticationHeader(String username, String password) {
//        String valueToEncode = username + ":" + password;
//        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
//    }
//
//    public class DataToChangeTariff {
//        private String msisdn;
//        private Long tariffId;
//
//        public String getMsisdn() {
//            return msisdn;
//        }
//
//        public void setMsisdn(String msisdn) {
//            this.msisdn = msisdn;
//        }
//
//        public Long getTariffId() {
//            return tariffId;
//        }
//
//        public void setTariffId(Long tariffId) {
//            this.tariffId = tariffId;
//        }
//
//        public DataToChangeTariff(String msisdn, Long tariffId) {
//            this.msisdn = msisdn;
//            this.tariffId = tariffId;
//        }
//
//        @Override
//        public String toString() {
//            return "DataToChangeRate{" + "msisdn='" + msisdn + '\'' +
//                    ", tariffId=" + tariffId +
//                    '}';
//
//        }
//    }
}
