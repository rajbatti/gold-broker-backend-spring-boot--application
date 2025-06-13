package com.hugoserve.demo.configuration;

import com.google.protobuf.util.JsonFormat;
import com.hugoserve.demo.proto.AssetTrasactionRequestDTO;
import com.hugoserve.demo.proto.DataDTO;
import com.hugoserve.demo.proto.HistoricPriceDTO;
import com.hugoserve.demo.proto.MetalDataPaginatedDTO;
import com.hugoserve.demo.proto.PerformancesDTO;
import com.hugoserve.demo.proto.response.AssetTrasactionResponseDTO;
import com.hugoserve.demo.proto.response.WalletResponseDTO;
import com.hugoserve.demo.proto.response.WalletTrasactionResponseDTO;
import com.hugoserve.demo.utils.ProtoMessageConverter;
import com.hugoserve.demo.proto.AuthRequestDTO;
import com.hugoserve.demo.proto.AuthResponseDTO;
import com.hugoserve.demo.proto.HistoricalSpotPrice;
import com.hugoserve.demo.proto.MetalDataDTO;
import com.hugoserve.demo.proto.MetalPriceDTO;
import com.hugoserve.demo.proto.PriceDTO;
import com.hugoserve.demo.proto.UserDetailsDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    @Bean
    public ProtoMessageConverter messageConverter() {
        JsonFormat.TypeRegistry typeRegistry = JsonFormat.TypeRegistry.newBuilder()
                .add(UserDetailsDTO.getDescriptor())
                .add(AuthRequestDTO.getDescriptor())
                .add(AuthResponseDTO.getDescriptor())
                .add(MetalPriceDTO.getDescriptor())
                .add(PriceDTO.getDescriptor())
                .add(HistoricalSpotPrice.getDescriptor())
                .add(MetalDataDTO.getDescriptor())
                .add(WalletResponseDTO.getDescriptor())
                .add(WalletTrasactionResponseDTO.getDescriptor())
                .add(AssetTrasactionResponseDTO.getDescriptor())
                .add(AssetTrasactionRequestDTO.getDescriptor())
                .add(HistoricPriceDTO.getDescriptor())
                .add(PerformancesDTO.getDescriptor())
                .add(DataDTO.getDescriptor())
                .add(MetalDataPaginatedDTO.getDescriptor())
                .build();
        return new ProtoMessageConverter(typeRegistry);
    }

    @Bean
    @Primary
    public ProtobufHttpMessageConverter getConverter(ProtoMessageConverter messageConverter) {
        return messageConverter.getHttpConverter(true);
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new ProtobufHttpMessageConverter());
        return restTemplate;
    }


}
