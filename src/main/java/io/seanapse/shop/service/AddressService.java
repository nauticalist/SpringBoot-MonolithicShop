package io.seanapse.shop.service;

import io.seanapse.shop.domain.Address;
import io.seanapse.shop.service.dto.AddressDto;

public class AddressService {
    public static AddressDto mapToDto(Address address) {
        if (address != null) {
            return new AddressDto(
                    address.getAddress1(),
                    address.getAddress2(),
                    address.getCity(),
                    address.getPostcode(),
                    address.getCountry()
            );
        }

        return null;
    }
}
